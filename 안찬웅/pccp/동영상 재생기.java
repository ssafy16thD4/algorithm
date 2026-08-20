import java.util.*;
/*
    10초전이동, 10초후 이동, 오프닝 거넌뛰기
    
    기능 3가지
    1. 명령어 배열을 돈다
    1. 항시: 현재재생위치가 op_start <= 현재재생위치 <= op_end일때 오프닝이 끝나는 위치로 이동
     1.1 prev: 현재위치에서 10초전으로 이동. 
        남은시간이 10초미만인 경우 영상의 처음위치로 이동(0분 0초)
     1.2 next: 현재위치에서 10초후로 이동. 
        남은시간이 10초미만일 경우 영상의 마지막위치(동영상의 길이와 같음)로 이동
     2. 명령어가 끝나고 현재시간을 00:00 형태로 출력
        
    video_len: 동영상의 길이   
    pos: 기능이 수행되기 직전의 재생위치 
    op_start: 오프닝 시작 시각 
    op_end: 오프닝이 끝나는 시각 
    commands: 사용자의 입력
*/
class Solution {
    public String solution(String video_len, String pos, String op_start, String op_end, String[] commands) {
        String answer = "";

        // op_start도 분으로 변경
        // op_end도 분으로 변경
        int start = translateTime(op_start);
        int end = translateTime(op_end);
        int videlTime = translateTime(video_len);
        
        // 1. 항시: 현재재생위치가 op_start <= 현재재생위치 <= op_end일때 오프닝이 끝나는 위치로 이동
        //  1.1 prev: 현재위치에서 10초전으로 이동. 
        //     남은시간이 10초미만인 경우 영상의 처음위치로 이동(0분 0초)
        //  1.2 next: 현재위치에서 10초후로 이동. 
        //     남은시간이 10초미만일 경우 영상의 마지막위치(동영상의 길이와 같음)로 이동
        //  2. 명령어가 끝나고 현재시간을 00:00 형태로 출력
        // 현재시간을 분으로 변경함
        int curTime = translateTime(pos);
        for(int i=0; i<commands.length; i++) {
            if(start <= curTime && curTime <= end) {
                // 오프닝이 끝나는 위치로 이동
                curTime = end;
            }
            if(commands[i].equals("prev")) {
                curTime -= 10;
                if(curTime < 10) {
                    curTime = 0;
                }
                
            } else if(commands[i].equals("next")) {
                curTime += 10;
                if(curTime < 10) {
                    curTime = videlTime;
                }
            }
        }
        // 오프닝이 끝나는 위치로 이동
        if(start <= curTime && curTime <= end) {
            curTime = end;
        }
        //System.out.println(curTime);
        
        int t = curTime / 60;
        int m = curTime % 60;
        
        // 출력된 값을 00:00 형태로 변경함
        StringBuilder sb = new StringBuilder();
        if (0 <= t && t < 10) {
            sb.append("0");
        }
        sb.append(String.valueOf(t));
        sb.append(":");
        
        if (0 <= m && m < 10) {
            sb.append("0");
        }
        sb.append(String.valueOf(m));

        return sb.toString();
    }
    static int translateTime(String pos) {
        String[] str = pos.split(":");
        int time = Integer.parseInt(str[0]);
        int min = Integer.parseInt(str[1]);
        int total = time * 60 + min;
        return total;
    }
}