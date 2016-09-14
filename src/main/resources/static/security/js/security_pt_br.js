angular.module('app.services').
value('lang', {

    _LOGIN_ERROR: 'Erro no login.',
    _LOGIN_CREATE:'Crie uma conta',
    _LOGIN_FORGOT:'Esqueceu sua senha?',
    _LOGIN_JOIN:'Ainda não possui acesso?',
    _LOGIN_REMEMBER:'Lembrar senha',
    _LOGIN_WITH:'Entrar com ',

	//geral
	ENTITY_NOT_FOUND:'Entidade não existe, não é possível cadastrar!',
	ENTITY_EXISTS:'Entidade já existe, não poderá criá-la!',
	ISSUE_DATE:'Incluído em',
	LOGIN_WITH:'Login com',
	NEXT_CHECK_DATE:'Próxima verificação',
	INTERNAL_NUMBER:"Número" ,
	COMPLETE:"Progresso" ,
	OWNER:"Responsável" ,
	PASSWORD_CONFIRMATION_TYPE: "Como você vai iniciar" ,
	PASSWORD_SUBMIT:"Concluir" ,
	PASSWORD_UPDATE:"Complete seu registro" ,
	PRINCIPAL:"Seu nome de usuário" ,

    bad_credentials: "Senha inválida",

	_getLocalizationKeys: function() {
		var keys = {};
		for (var k in this) {
			keys[k] = k;
		}
		return keys;
	}
});
