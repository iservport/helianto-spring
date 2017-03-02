package org.helianto.ingress.service

import org.helianto.core.service.EntityInstallService
import org.helianto.ingress.domain.Invite
import org.helianto.ingress.repository.InviteRepository
import org.helianto.user.domain.User
import org.helianto.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class InviteService(repository: InviteRepository, installService: EntityInstallService, userRepository: UserRepository) {

  val INVITE_VALIDITY_DAYS = 3

  def get(id: String): Invite =
    Option(repository.findOne(id)) match {
      case Some(invite) => invite
      case None => throw new IllegalArgumentException(s"Unable to find invite with id $id.")
    }

  def getUser(userId: String): User =
    Option(repository.findInvitedUserByUserId(userId)) match {
      case Some(user) => user
      case None => throw new IllegalArgumentException(s"Unable to find user with id $userId.")
    }

  def save(command: Invite): Invite =
    Option(repository.findOne(command.getId)) match {
      case Some(invite) =>
        if (invite.isValidTo(INVITE_VALIDITY_DAYS) && !invite.isConfirmed) invite
        else throw new IllegalArgumentException("Unable to save, invite already exists and is no longer valid.")
      case None => repository.saveAndFlush(command)
    }

  /**
    * Method called after th invitee has accepted the invitation.
    *
    * <p>Before creating a new entity, the invite must be checked to ensure it is valid within INVITE_VALIDITY_DAYS and
    * it was never used.</p>
    *
    * @param command
    * @return
    */
  def update(command: Invite) =
    Option(repository.findOne(command.getId)) match {
      case Some(invite) =>
        if (invite.isConfirmed)                      throw new IllegalArgumentException("The invitation has been already accepted")
        if (!invite.isValidTo(INVITE_VALIDITY_DAYS)) throw new IllegalArgumentException(s"The invitation has expired in ${invite.getValidTo(INVITE_VALIDITY_DAYS)}")
        val acceptedInvite = invite.merge(command)
        Option(installService.install(acceptedInvite)) match {
          case Some(entity) =>
            val user = findUser(entity.getId, invite.getPrincipal)
            repository.saveAndFlush(acceptedInvite.confirm(user.getId))
          case None =>                               throw new IllegalArgumentException("Unable to create entity.")
        }
        repository.saveAndFlush(command)
      case None =>                                   throw new IllegalArgumentException("Unable to update, invite does not exist.")
    }

  private def findUser(entityId: String, principal: String) = userRepository.findByEntityIdAndUserKey(entityId, principal)

}
