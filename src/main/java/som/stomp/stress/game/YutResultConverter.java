package som.stomp.stress.game;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class YutResultConverter {

    public static String intToString(String yutResult){
        switch (yutResult) {
            case "0" -> {
                return "BACK_DO";
            }
            case "1" -> {
                return "DO";
            }
            case "2" -> {
                return "GAE";
            }
            case "3" -> {
                return "GIRL";
            }
            case "4" -> {
                return "YUT";
            }
            case "5" -> {
                return "MO";
            }
            default -> {
                throw new RuntimeException("윷 결과가 0~5를 벗어남: " + yutResult);
            }
        }

//        if(yutResult.equals("0")){
//            return "BACK_DO";
//        }
//        else if(yutResult.equals("1")){
//            return "DO";
//        }
//        else if(yutResult.equals("2")){
//            return "GIRL";
//        }
//        else if(yutResult.equals("3")){
//            return "DO";
//        }
//        else if(yutResult.equals("4")){
//            return "YUT";
//        }
//        else if(yutResult.equals("5")){
//            return "MO";
//        }
//        else {
//            throw new RuntimeException("윷 결과가 0~5를 벗어남: " + yutResult);
////            log.error("윷 결과가 0~5를 벗어남: {}", yutResult);
////            return yutResult;
//        }
    }

    public String intToString(int yutResult){
        switch (yutResult) {
            case 0 -> {
                return "BACK_DO";
            }
            case 1 -> {
                return "DO";
            }
            case 2 -> {
                return "GAE";
            }
            case 3 -> {
                return "GIRL";
            }
            case 4 -> {
                return "YUT";
            }
            case 5 -> {
                return "MO";
            }
            default -> {
                throw new RuntimeException("윷 결과가 0~5를 벗어남: " + yutResult);
            }
        }
    }
}
