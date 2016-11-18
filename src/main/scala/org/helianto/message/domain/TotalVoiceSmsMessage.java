package org.helianto.message.domain;

/**
 * TotalVoice Sms Message.
 */
public class TotalVoiceSmsMessage {

    private String numero_destino;

    private String mensagem = "";

    private boolean resposta_usuario = false;

    public TotalVoiceSmsMessage() {
        super();
    }

    public TotalVoiceSmsMessage(String numero_destino, String mensagem) {
        this();
        this.numero_destino = numero_destino;
        this.mensagem = mensagem;
    }

    public TotalVoiceSmsMessage(String numero_destino, String mensagem, boolean resposta_usuario) {
        this(numero_destino, mensagem);
        this.resposta_usuario = resposta_usuario;
    }

    public String getNumero_destino() {
        return this.numero_destino;
    }

    public String getMensagem() {
        return this.mensagem;
    }

    public boolean isResposta_usuario() {
        return this.resposta_usuario;
    }

    public void setNumero_destino(String numero_destino) {
        this.numero_destino = numero_destino;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public void setResposta_usuario(boolean resposta_usuario) {
        this.resposta_usuario = resposta_usuario;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof TotalVoiceSmsMessage)) return false;
        final TotalVoiceSmsMessage other = (TotalVoiceSmsMessage) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$numero_destino = this.getNumero_destino();
        final Object other$numero_destino = other.getNumero_destino();
        if (this$numero_destino == null ? other$numero_destino != null : !this$numero_destino.equals(other$numero_destino))
            return false;
        final Object this$mensagem = this.getMensagem();
        final Object other$mensagem = other.getMensagem();
        if (this$mensagem == null ? other$mensagem != null : !this$mensagem.equals(other$mensagem)) return false;
        if (this.isResposta_usuario() != other.isResposta_usuario()) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $numero_destino = this.getNumero_destino();
        result = result * PRIME + ($numero_destino == null ? 43 : $numero_destino.hashCode());
        final Object $mensagem = this.getMensagem();
        result = result * PRIME + ($mensagem == null ? 43 : $mensagem.hashCode());
        result = result * PRIME + (this.isResposta_usuario() ? 79 : 97);
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof TotalVoiceSmsMessage;
    }

    public String toString() {
        return "org.helianto.message.domain.TotalVoiceSmsMessage(numero_destino=" + this.getNumero_destino() + ", mensagem=" + this.getMensagem() + ", resposta_usuario=" + this.isResposta_usuario() + ")";
    }
}
