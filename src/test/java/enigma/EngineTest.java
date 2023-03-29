package enigma;

import enigma.Engine;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EngineTest {

    final private String encodedMessage = "NRPTVLSCRMXYTGSPRFLTC";

    @Test
    void encode() {
        Engine encodeEngine = new Engine(1, 2, 3, 1, "AAA");
        String messageToEncode = "This is an enigma machine";
        String testEncodedMessage = encodeEngine.encode(messageToEncode);
        assertEquals(encodedMessage,testEncodedMessage);
    }

    @Test
    void decode() {
        Engine decodeEngine = new Engine(1, 2, 3, 1, "AAA");
        String testDecodedMessage = decodeEngine.encode(encodedMessage);
        String decodedMessage = "THISISANENIGMAMACHINE";
        assertEquals(decodedMessage,testDecodedMessage);

    }
}