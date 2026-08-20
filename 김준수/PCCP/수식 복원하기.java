/*
결과 있는 수식이 2 ~ 9진법일때 가능한 숫자들을 전부 넣고
결과랑 비교해서 2~9진법일떄 가능하면 true, 불가능하면 false를 채워
10짜리 길이 배열로 해서 0 1은 무시하고 2가 가능하면 true, 9가 가능하면 false 이런 식으로
그리고 전부 다 돌면서 현재 가능한 진법만 결과 리스트에 담아놔
이때 만약 현재 left나 right를 구성하는 수가 현재 도는 진법보다 같거나 큰 수가 존재하면 그냥 false로 처리
예를 들어 14면 2진수로 표현이 안되는 4가 있기 때문에 false

결과 없는 수식이 [가능한 진법]일때 나올 수 있는 결과들을 만들어보고
만약 결과의 수가 2개 이상이면 ?, 결과의 수가 1개면 그 수를 넣으면 됨
그럼 이제 돌면서 

정답 수식의 [가능한 진법]을 저장하는 boolean[정답 수식 수][10]
최종적으로 정답 수식을 기반으로 가능한 진법을 저장하는 List<Integer>
문제 수식이 가능 진법에 대해 계산했을 때 나올 결과물을 저장하는 int[문제 수식 수][10]
*/

import java.util.*;

class Solution {
    
    class Exp{
        int left;
        int right;
        int result;
        String calc;
        boolean isComplete;
        List<Integer> digits;
        
        Exp(int left, String calc, int right, int result){
            this.left = left;
            this.calc = calc;
            this.right = right;
            this.result = result;
            isComplete = true;
            digits = new ArrayList<>();
        }
        
        Exp(int left, String calc, int right){
            this.left = left;
            this.calc = calc;
            this.right = right;
            this.result = -1;
            isComplete = false;
            digits = new ArrayList<>();
        }
    }
    
    public String[] solution(String[] expressions) {
        // 수식 객체 만들기
        Exp[] exps = new Exp[expressions.length];
        for(int i = 0; i < exps.length; i++){
            String[] components = expressions[i].split(" ");
            if(components[4].equals("X")){
                exps[i] = new Exp(Integer.parseInt(components[0]), components[1], 
                                  Integer.parseInt(components[2]));
            }
            else{
                exps[i] = new Exp(Integer.parseInt(components[0]), components[1], 
                                  Integer.parseInt(components[2]), Integer.parseInt(components[4]));
            }
        }
        
        // 정답 수식의 각 진법에 대한 결과 계산 후 가능하면 digit에 추가
        boolean[][] canDigit = new boolean[exps.length][10];
        for(int i = 0; i < exps.length; i++){
            for(int d = 2; d < 10; d++){
                // 진법의 수보다 큰 수가 존재하면 스킵
                List<Integer> nums = new ArrayList<>();
                nums.add(exps[i].left % 10);
                nums.add(exps[i].left / 10);
                nums.add(exps[i].right % 10);
                nums.add(exps[i].right / 10);
                
                boolean flag = false;
                for(int num : nums){
                    if(num >= d) flag = true;
                }
                if(flag) break;
                System.out.println(i + " " + d);
                
                // 해당 진법에 맞춰서 수식 계산
                int left = Integer.parseInt(Integer.toString(exps[i].left, d));
                System.out.println(left + " " + d + " " + i);
                
                // 진법 계산 결과와 실제 결과가 같으면 가능 진법에 추가
            }
        }
        
        
        String[] answer = {};
        return answer;
    }
}