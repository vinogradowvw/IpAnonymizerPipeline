package ipanonymizerpipeline;

/*
 * The anonymizer class (just inserting X in the last part of ip)
 */


public class Anonymizer {

    public static String anonymizeIp(String ip) {
        
        String[] _ip = ip.split("\\.");
    
        StringBuilder anonymizedIp = new StringBuilder();

        for (int i = 0; i <= 2; i++) {
            anonymizedIp.append(_ip[i]).append(".");
        }

        anonymizedIp.append("X");

        return anonymizedIp.toString();
    }

}
