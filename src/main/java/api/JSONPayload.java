package api;

public class JSONPayload{
    private Data data = new Data();
    private Message message = new Message();

    Message getMessage() {
        return message;
    }

    private class Data{
        private Settings settings = new Settings();
    }
    class Message{
        private String plainText = "";
        private String encryptedText= "";
        private int messageId = 0;


        public int getMessageId() {
            return messageId;
        }

        void setMessageId(int messageId) {
            this.messageId = messageId;
        }

        String getPlainText() {
            return plainText;
        }

        void setPlainText(String plainText) {
            this.plainText = plainText;
        }

        String getEncryptedText() {
            return encryptedText;
        }

        void setEncryptedText(String encryptedText) {
            this.encryptedText = encryptedText;
        }
    }
    private class Settings{
        private int leftRotor = 0;
        private int middleRotor = 0;
        private int rightRotor = 0;
        private int reflector = 0;
        private String counterSettings = "AAA";
    }

    int[] getRotorsAndReflector(){
        return new int[]{data.settings.leftRotor, data.settings.middleRotor, data.settings.rightRotor, data.settings.reflector};
    }
    String getCounterSettings(){
        return data.settings.counterSettings;
    }
    public JSONPayload(){

    }
}