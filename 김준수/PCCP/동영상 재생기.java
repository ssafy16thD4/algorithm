class Solution {
    public String solution(String video_len, String pos, String op_start, String op_end, String[] commands) {
        String answer = "";
        int end = toSecond(video_len);
        int opStart = toSecond(op_start);
        int opEnd = toSecond(op_end);
        int curr = toSecond(pos);
        
        if(curr >= opStart && curr <= opEnd) curr = opEnd;
        
        for(String command : commands){
            switch(command){
                case "prev":
                    curr -= 10;
                    break;
                case "next":
                    curr += 10;
                    break;
            }
            
            if(curr < 0) curr = 0;
            else if(curr > end) curr = end;
            
            if(curr >= opStart && curr < opEnd) curr = opEnd;
        }
        
        return String.format("%02d:%02d", curr / 60, curr % 60);
    }
    
    private int toSecond(String time){
        return Integer.parseInt(time.substring(0, 2)) * 60 + Integer.parseInt(time.substring(3, 5));
    }
}