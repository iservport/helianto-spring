package org.helianto.security.service

import java.util.Date

import org.helianto.core.domain.Identity
import org.helianto.security.domain.{Secret, UserAuthority, UserDetailsAdapter}
import org.helianto.security.repository.{SecretRepository, UserAuthorityRepository}
import org.helianto.test.UnitSpec
import org.helianto.user.domain.User
import org.helianto.user.domain.enums.UserState
import org.helianto.user.repository.{UserProjection, UserRepository}
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder

/**
  * Test conversion of user authorities to strings.
  *
  * UserAuthority is the persistent domain object that summarizes roles for one service.
  * We need to expand such roles to a set of Strings to be used as GrantedAuthorites by Spring
  * Security.
  */
class UserAuthoritiesAsStringTests extends UnitSpec {

  val authority: UserAuthority = new UserAuthority {
    override def getId: String = "UUID"
    override def getServiceCode: String = "service"
    override def getUserId: String = "USER-ID"
  }

  "An UserAuthority Set " should "be a set of formatted strings based on an UserAuthority instance " in {
    authority.setServiceExtension("")
    val userAuthoritySet = UserDetailsServiceImpl.getUserAuthoritiesAsString(authority)
    userAuthoritySet should contain theSameElementsAs Set("ROLE_SERVICE_")
  }

  it should "grow with extensions" in {
    authority.setServiceExtension("EXT1")
    val userAuthoritySet = UserDetailsServiceImpl.getUserAuthoritiesAsString(authority)
    userAuthoritySet should contain theSameElementsAs Set("ROLE_SERVICE_EXT1")
  }

  it should "convert to upper case" in {
    authority.setServiceExtension("ext1")
    val userAuthoritySet = UserDetailsServiceImpl.getUserAuthoritiesAsString(authority)
    userAuthoritySet should contain theSameElementsAs Set("ROLE_SERVICE_EXT1")
  }

  it should "trim spaces" in {
    authority.setServiceExtension("ext1 ")
    val userAuthoritySet = UserDetailsServiceImpl.getUserAuthoritiesAsString(authority)
    userAuthoritySet should contain theSameElementsAs Set("ROLE_SERVICE_EXT1")
  }

  it should "allow for comma separated values" in {
    authority.setServiceExtension("ext1 , ExT 2")
    val userAuthoritySet = UserDetailsServiceImpl.getUserAuthoritiesAsString(authority)
    userAuthoritySet should contain theSameElementsAs Set("ROLE_SERVICE_EXT1", "ROLE_SERVICE_EXT2")
  }

  it should "tolerate null extensions" in {
    authority.setServiceExtension(null)
    val userAuthoritySet = UserDetailsServiceImpl.getUserAuthoritiesAsString(authority)
    userAuthoritySet should contain theSameElementsAs Set("ROLE_SERVICE_")
  }

}

/**
  * Test updating the User representation as required by Spring Security.
  *
  * Once we have a set of strings to tell our roles, we must update the user details
  * with a set of GrantedAuthorities.
  */
class UpdateAuthoritiesTests extends UnitSpec {

  import org.mockito.Mockito._

  import collection.JavaConverters._

  val service = new UserDetailsServiceImpl {
    override val userAuthorityRepository = mock[UserAuthorityRepository]
  }

  val userDetails = new UserDetailsAdapter {
    override val getIdentityId = "IDENTITY"
  }

  "After updateAuthorities, UserDetails " should "contain a list of GrantedAuthorities " in {
    val adapterList = List(new UserAuthority("PARENT", "SERVICE", "A")).asJava
    when(service.userAuthorityRepository.findByUserIdOrderByServiceCodeAsc(userDetails.getUserId))
      .thenReturn(adapterList)
    val user = service.updateAuthorities(userDetails)
    val expected = Seq("SELF_ID_IDENTITY", "ROLE_SERVICE_A")
      .map(e => new SimpleGrantedAuthority(e).asInstanceOf[GrantedAuthority])
    user.getAuthorities should contain theSameElementsAs expected
  }

  it should "not repeat roles, even if they come from different groups " in {
    val adapterList = List(new UserAuthority("ONE", "SERVICE", "A"), new UserAuthority("TWO", "SERVICE", "A,B")).asJava
    when(service.userAuthorityRepository.findByUserIdOrderByServiceCodeAsc(userDetails.getUserId))
      .thenReturn(adapterList)
    val user = service.updateAuthorities(userDetails)
    val expected = Seq("SELF_ID_IDENTITY", "ROLE_SERVICE_A", "ROLE_SERVICE_B")
      .map(e => new SimpleGrantedAuthority(e).asInstanceOf[GrantedAuthority])
    user.getAuthorities should contain theSameElementsAs expected
  }

  it should "tolerate empty authorities" in {
    when(service.userAuthorityRepository.findByUserIdOrderByServiceCodeAsc(userDetails.getUserId))
      .thenReturn(List[UserAuthority]().asJava)
    val user = service.updateAuthorities(userDetails)
    user.getAuthorities should contain theSameElementsAs Seq(new SimpleGrantedAuthority("SELF_ID_IDENTITY"))
  }

  it should "tolerate null authorities" in {
    when(service.userAuthorityRepository.findByUserIdOrderByServiceCodeAsc(userDetails.getUserId))
      .thenReturn(null)
    val user = service.updateAuthorities(userDetails)
    user.getAuthorities should contain theSameElementsAs Seq(new SimpleGrantedAuthority("SELF_ID_IDENTITY"))
  }

}

/**
  * The required password is kept in a class apart from user and must be fetched.
  */
class LoadIdentitySecretTests extends UnitSpec {

  import org.mockito.Mockito._

  val service = new UserDetailsServiceImpl {
    override val secretRepository = mock[SecretRepository]
    def test(username: String) = loadIdentitySecretByKey(username)
  }

  "Any username" should "correspond to a valid secret to be supplied to Spring Security" in {
    val secret = new Secret
    when(service.secretRepository.findByIdentityKeyOrEmail("username"))
      .thenReturn(secret)
    assert(service.test("username")==secret)
  }

  it should "fail if no secret is found" in {
    when(service.secretRepository.findByIdentityKeyOrEmail("username"))
      .thenReturn(null)
    intercept[UsernameNotFoundException] { service.test("username") }
  }

}

/**
  * One username may link to 0 or more actual users.
  *
  * The 'last' user is actually the first on the list, as it is in reverse order.
  */
class LoadLastUserTests extends UnitSpec {

  import org.mockito.Mockito._

  import collection.JavaConverters._

  val service = new UserDetailsServiceImpl {
    override val userRepository = mock[UserRepository]
    def test(identityId: String) = loadLastUserByIdentityId(identityId)
  }

  case class UserTest(userId: String) extends UserProjection {
    override def getUserId: String = userId
    override def getIdentityId: String = ""
    override def getUserName: String = ""
    override def getUserState: UserState = UserState.ACTIVE
    override def isAccountNonExpired: Boolean = true
    override def getEntityId: String = ""
  }

  "Any username" should "load the first from an user list" in {
    val userList: java.util.List[UserProjection] = List(UserTest("1"), UserTest("2")).map(_.asInstanceOf[UserProjection]).asJava
    when(service.userRepository.findByIdentity_IdOrderByLastEventDesc("identityId"))
      .thenReturn(userList)
    assert(service.test("identityId")==UserTest("1"))
  }

  it should "fail if list is empty" in {
    val emptyList: java.util.List[UserProjection] = new java.util.ArrayList[UserProjection]()
    when(service.userRepository.findByIdentity_IdOrderByLastEventDesc("identityId"))
      .thenReturn(emptyList)
    intercept[UsernameNotFoundException] { service.test("identityId") }
  }

  it should "fail if list is null" in {
    when(service.userRepository.findByIdentity_IdOrderByLastEventDesc("identityId"))
      .thenReturn(null)
    intercept[UsernameNotFoundException] { service.test("identityId") }
  }

}

/**
  * Before returning a valid UserDetails to Spring Security, the corresponding User
  * must be updated to reflect the current Date as the last event.
  *
  * Once the last event is set, next security check will point to this user again.
  */
class UpdateLastEventTests extends UnitSpec {

  import org.mockito.Mockito._

  val service = new UserDetailsServiceImpl {
    override val userRepository = mock[UserRepository]
    def test(userDetails: UserDetailsAdapter) = updateLastEvent(userDetails)
  }

  val userDetails = new UserDetailsAdapter {
    override val getUserId = "USER"
  }

  "On load success, the user" should "be updated to reflect the successful login instant" in {
    val user = new User
    val min = new Date
    when(service.userRepository.findOne(userDetails.getUserId))
      .thenReturn(user)
    when(service.userRepository.saveAndFlush(user.updateLastEvent()))
      .thenReturn(user)
    service.test(userDetails)
    assert(user.getLastEvent.after(min))
    val max = new Date
    assert(!user.getLastEvent.after(max))
  }

  it should "fail if user is not found" in {
    when(service.userRepository.findOne(userDetails.getUserId))
      .thenReturn(null)
    intercept[UsernameNotFoundException] { service.test(userDetails) }
  }

}

/**
  * A simple test to assure the final assembly.
  */
class LoadUserByUsernameTests extends UnitSpec {

  case class UserTest(userId: String) extends UserProjection {
    override def getUserId: String = userId
    override def getIdentityId: String = ""
    override def getUserName: String = "usernameFromDb"
    override def getUserState: UserState = UserState.ACTIVE
    override def isAccountNonExpired: Boolean = true
    override def getEntityId: String = ""
  }
  val encoder = new PasswordEncoder {
    override def matches(rawPassword: CharSequence, encodedPassword: String): Boolean = true
    override def encode(rawPassword: CharSequence): String = rawPassword.toString.toUpperCase
  }
  val identity = new Identity
  val secret = new Secret(identity, "")
  secret.encode(encoder, "password")

  "All functions" should "collaborate to provide valid user details" in {
    val service = new UserDetailsServiceImpl {
      override def loadIdentitySecretByKey(userKey: String) = secret
      override def loadLastUserByIdentityId(userKey: String) = UserTest("1")
      override def updateLastEvent(userDetails: UserDetailsAdapter) = {
        assert(userDetails.getUserId=="1")
      }
      override def updateAuthorities(userDetails: UserDetailsAdapter) = userDetails
      def test(userKey: String) = loadUserByUsername(userKey)
    }
    val userDetails = service.loadUserByUsername("username")
    assert(userDetails.getUsername=="usernameFromDb")
    assert(userDetails.getPassword=="PASSWORD")
  }
}

//def loadUserByUsername(userKey: String): UserDetails = {
//  val secret = loadIdentitySecretByKey(userKey)
//  val user = loadLastUserByIdentityId(secret.getIdentity.getId)
//  val userDetails = new UserDetailsAdapter(user, secret)
//  updateLastEvent(userDetails)
//  updateAuthorities(userDetails)
//}
