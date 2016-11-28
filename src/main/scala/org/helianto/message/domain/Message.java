package org.helianto.message.domain;

import java.util.UUID;

/**
 * Simple message.
 */
public class Message {

    private ContactData sender;

    private ContactData recipient;

    private String servicePath;

    private MessageData messageData = new MessageData();

    private MessageDefaults defaults = new MessageDefaults();

    private String id = UUID.randomUUID().toString().replaceAll("-", "");

    private String token = "";

    private String template = "4853b17b-e5f2-4b8f-b313-7067088fb3c5";

    public Message() {
        super();
    }

    public Message(ContactData sender, ContactData recipient, String servicePath, MessageData messageData, MessageDefaults defaults) {
        this();
        this.sender = sender;
        this.recipient = recipient;
        this.servicePath = servicePath;
        this.messageData = messageData;
        this.defaults = defaults;
    }

    public Message(ContactData sender, ContactData recipient, String servicePath, MessageData messageData, MessageDefaults defaults, String token) {
        this(sender, recipient, servicePath, messageData, defaults);
        this.token = token;
    }

    public Message(ContactData sender, ContactData recipient, String servicePath, MessageData messageData, MessageDefaults defaults, String token, String template) {
        this(sender, recipient, servicePath, messageData, defaults, token);
        this.template = template;
    }

    public ContactData getSender() {
        return this.sender;
    }

    public ContactData getRecipient() {
        return this.recipient;
    }

    public String getServicePath() {
        return this.servicePath;
    }

    public MessageData getMessageData() {
        return this.messageData;
    }

    public MessageDefaults getDefaults() {
        return this.defaults;
    }

    public String getId() {
        return this.id;
    }

    public String getToken() {
        return token;
    }

    public String getTemplate() {
        return this.template;
    }

    public void setSender(ContactData sender) {
        this.sender = sender;
    }

    public void setRecipient(ContactData recipient) {
        this.recipient = recipient;
    }

    public void setServicePath(String servicePath) {
        this.servicePath = servicePath;
    }

    public void setMessageData(MessageData messageData) {
        this.messageData = messageData;
    }

    public void setDefaults(MessageDefaults defaults) {
        this.defaults = defaults;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Message)) return false;
        final Message other = (Message) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$sender = this.getSender();
        final Object other$sender = other.getSender();
        if (this$sender == null ? other$sender != null : !this$sender.equals(other$sender)) return false;
        final Object this$recipient = this.getRecipient();
        final Object other$recipient = other.getRecipient();
        if (this$recipient == null ? other$recipient != null : !this$recipient.equals(other$recipient)) return false;
        final Object this$servicePath = this.getServicePath();
        final Object other$servicePath = other.getServicePath();
        if (this$servicePath == null ? other$servicePath != null : !this$servicePath.equals(other$servicePath))
            return false;
        final Object this$messageData = this.getMessageData();
        final Object other$messageData = other.getMessageData();
        if (this$messageData == null ? other$messageData != null : !this$messageData.equals(other$messageData))
            return false;
        final Object this$defaults = this.getDefaults();
        final Object other$defaults = other.getDefaults();
        if (this$defaults == null ? other$defaults != null : !this$defaults.equals(other$defaults)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$token = this.getToken();
        final Object other$token = other.getToken();
        if (this$token == null ? other$token != null : !this$token.equals(other$token)) return false;
        final Object this$template = this.getTemplate();
        final Object other$template = other.getTemplate();
        if (this$template == null ? other$template != null : !this$template.equals(other$template)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $sender = this.getSender();
        result = result * PRIME + ($sender == null ? 43 : $sender.hashCode());
        final Object $recipient = this.getRecipient();
        result = result * PRIME + ($recipient == null ? 43 : $recipient.hashCode());
        final Object $servicePath = this.getServicePath();
        result = result * PRIME + ($servicePath == null ? 43 : $servicePath.hashCode());
        final Object $messageData = this.getMessageData();
        result = result * PRIME + ($messageData == null ? 43 : $messageData.hashCode());
        final Object $defaults = this.getDefaults();
        result = result * PRIME + ($defaults == null ? 43 : $defaults.hashCode());
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $token = this.getToken();
        result = result * PRIME + ($token == null ? 43 : $token.hashCode());
        final Object $template = this.getTemplate();
        result = result * PRIME + ($template == null ? 43 : $template.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Message;
    }

    public String toString() {
        return "org.helianto.message.domain.Message(sender=" + this.getSender() + ", recipient=" + this.getRecipient() + ", servicePath=" + this.getServicePath() + ", messageData=" + this.getMessageData() + ", defaults=" + this.getDefaults() + ", id=" + this.getId() + ", token=" + this.getToken() + ", template=" + this.getTemplate() + ")";
    }
}
