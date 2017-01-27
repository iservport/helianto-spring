/**
 * Validate PIN (Personal Identification Number)
 */
$scope.validatePin = function (pin, pinValidator) {

    if (pinValidator=="CPF") {
        if(pin == null || pin == 'undefined' || pin == '') return false;
        pin = pin.replace(/[^\d]+/g,'');
        if (pin.length != 11) return false;
        if (pin == "00000000000") return false;

        var Soma = 0;
        var Resto;
        for (i=1; i<=9; i++) Soma = Soma + parseInt(pin.substring(i-1, i)) * (11 - i);
        Resto = (Soma * 10) % 11;
        if ((Resto == 10) || (Resto == 11)) Resto = 0;
        if (Resto != parseInt(pin.substring(9, 10)) ) return false;

        Soma = 0;
        for (i = 1; i <= 10; i++) Soma = Soma + parseInt(pin.substring(i-1, i)) * (12 - i);
        Resto = (Soma * 10) % 11;
        if ((Resto == 10) || (Resto == 11)) Resto = 0;
        if (Resto != parseInt(pin.substring(10, 11) ) ) return false;

        return true;
    }
    else {
        return true;
    }

}

/**
 * Validate PUN (Public Unique Number)
 */
$scope.validatePun = function(pun, punValidator){

    if (punValidator=="CNPJ") {
        if(pun == null || pun == 'undefined' || pun == '') return false;
        pun = pun.replace(/[^\d]+/g,'');
        if (pun.length != 14) return false;
        // known as invalid
        if (pun == "00000000000000" ||
            pun == "11111111111111" ||
            pun == "22222222222222" ||
            pun == "33333333333333" ||
            pun == "44444444444444" ||
            pun == "55555555555555" ||
            pun == "66666666666666" ||
            pun == "77777777777777" ||
            pun == "88888888888888" ||
            pun == "99999999999999")
            return false;

        // Validate digits
        netSize = pun.length - 2
        numeros = pun.substring(0,netSize);
        digitos = pun.substring(netSize);

        soma = 0;
        pos = netSize - 7;
        for (i = netSize; i >= 1; i--) {
          soma += numeros.charAt(netSize - i) * pos--;
          if (pos < 2) pos = 9;
        }
        resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
        if (resultado != digitos.charAt(0)) return false;

        netSize = netSize + 1;
        numeros = pun.substring(0,netSize);

        soma = 0;
        pos = netSize - 7;
        for (i = netSize; i >= 1; i--) {
          soma += numeros.charAt(netSize - i) * pos--;
          if (pos < 2) pos = 9;
        }
        resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
        if (resultado != digitos.charAt(1)) return false;

        return true;
    }
    else {
        return true;
    }

}
