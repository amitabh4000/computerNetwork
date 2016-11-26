package ComputerNetworks.clientServer;

import java.util.ArrayList;

public class Utils {
    public static final String BASE_PATH = "C:/Users/SAmitabh/IdeaProjects/General_Programming/src/ComputerNetworks/clientServer/Client";
    public static String[] splitIt(String message){
        ArrayList<String> result = new ArrayList<>();
        int ind = 0;
        StringBuilder sb = new StringBuilder();

        while(ind <= message.length()){
            if(ind == message.length() ||message.charAt(ind) == ' ' ){
                if(sb != null && sb.length() != 0) result.add(sb.toString());
                sb = new StringBuilder();
            }
            else if(message.charAt(ind) == '"' ){
                System.out.println("In quotes");
                sb = new StringBuilder();

                while(message.charAt(++ind)!= '"'){
                    sb.append(message.charAt(ind));
                }
            }
            else{
                sb.append(message.charAt(ind));
            }
            ind++;
        }
        return result.toArray(new String[result.size()]);
    }

////////////////////////////////////////////////////////////////

    public static int getInt(String s){

        int ind = s.length()-1;
        int res = 0;
        int place = 1;
        while(Character.isDigit(s.charAt(ind))){
            res = place * s.charAt(ind)-'0' + res;
            place *= 10;
            ind--;
        }
        return res;
    }






}
