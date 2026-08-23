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
        
        @Override
        public String toString(){
            String resultStr = "";
            if(result == -1) resultStr = "?";
            else resultStr = Integer.toString(result);
            
            return String.format("%d %s %d = %s", left, calc, right, resultStr);
        }
    }
    
    public String[] solution(String[] expressions) {
        // 수식 객체 만들기
        // 결과 있는 수식의 수, 결과없는 수식의 수 세기
        Exp[] exps = new Exp[expressions.length];
        int existRCnt = 0;
        int nonRCnt = 0;
        for(int i = 0; i < exps.length; i++){
            String[] components = expressions[i].split(" ");
            if(components[4].equals("X")){
                exps[i] = new Exp(Integer.parseInt(components[0]), components[1], 
                                  Integer.parseInt(components[2]));
                nonRCnt++;
            }
            else{
                exps[i] = new Exp(Integer.parseInt(components[0]), components[1], 
                                  Integer.parseInt(components[2]), Integer.parseInt(components[4]));
                existRCnt++;
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
                nums.add(exps[i].result % 10);
                nums.add(exps[i].result / 100); // result는 세자리까지 가능
                nums.add(exps[i].result / 10 % 10);
                
                boolean flag = false;
                for(int num : nums){
                    if(num >= d) {
                        flag = true;
                        break;
                    }
                    
                }
                if(flag) continue;
                
                // 해당 진법에 맞춰서 수식 계산
                int left = Integer.parseInt(Integer.toString(exps[i].left), d);
                int right = Integer.parseInt(Integer.toString(exps[i].right), d);
                switch(exps[i].calc){
                    case "+":
                        left += right;
                        break;
                    case "-":
                        left -= right;
                        break;
                }
                
                // 진법 계산 결과와 실제 결과가 같으면 가능 진법에 추가
                if(left == Integer.parseInt(Integer.toString(exps[i].result), d)){
                    exps[i].digits.add(d);   
                }
            }
        }
        
        // 각 수식이 어떤 진법을 쓸 수 있는지를 세서
        // 최종적으로 전체에 다 적용할 수 있는 진법 찾기
        Map<Integer, Integer> canDigitMap = new HashMap<>();
        for(int i = 0; i < exps.length; i++){
            // System.out.println(exps[i].digits.toString());
            for(int d : exps[i].digits){
                canDigitMap.compute(d, (k, v) -> (v == null) ? 1 : v + 1);
            }
        }
        
        List<Integer> canDigitList = new ArrayList<>();
        for(int key : canDigitMap.keySet()){
            if(canDigitMap.get(key) == existRCnt){
                canDigitList.add(key);
            }
        }
        
        // 예외 처리
        // 결과가 있는 수식이 입력에 하나도 없으면
        // 가능한 진법을 2 ~ 9 모두로 해야 함
        if(existRCnt == 0){
            for(int i = 2; i <= 9; i++){
                canDigitList.add(i); 
            }
        }
        
         
        // 진법의 수보다 큰 수가 존재하면 해당 진법은 가능 목록에서 제거
        List<Integer> deleteDigits = new ArrayList<>();
        for(int i = 0; i < exps.length; i++){
            if(exps[i].isComplete) continue; // 결과 있는 수식이면 스킵
            
            List<Integer> nums = new ArrayList<>();
            nums.add(exps[i].left % 10);
            nums.add(exps[i].left / 10);
            nums.add(exps[i].right % 10);
            nums.add(exps[i].right / 10);
            
            for(int d : canDigitList){
                 
                boolean flag = false;
                for(int num : nums){
                    if(num >= d) {
                        deleteDigits.add(d);
                    }
                }
            }
        }
        for(int d : deleteDigits){
            if(canDigitList.contains(d)){
                canDigitList.remove(canDigitList.indexOf(d));
            }
        }
        
        // 결과 없는 수식에 가능한 진법 대입
        for(int i = 0; i < exps.length; i++){
            if(exps[i].isComplete) continue; // 결과 있는 수식이면 스킵
            
            Set<Integer> resultSet = new HashSet<>();
            
            for(int d : canDigitList){
                // 진법으로 계산한 결과를 Set에 저장
                int left = Integer.parseInt(Integer.toString(exps[i].left), d);
                int right = Integer.parseInt(Integer.toString(exps[i].right), d);
                switch(exps[i].calc){
                    case "+":
                        left += right;
                        break;
                    case "-":
                        left -= right;
                        break;
                }
                resultSet.add(Integer.parseInt(Integer.toString(left, d)));
            }
            // System.out.println(i + " " + resultSet.toString());
            
            // Set의 크기가 1이면 result를 해당 값으로 할당
            // Set의 크기가 2 이상이면 result를 -1로 유지
            if(resultSet.size() == 1){
                List<Integer> temp = new ArrayList<>(resultSet);
                exps[i].result = temp.get(0);
            }
        }
        
        List<String> answerList = new ArrayList<>();
        for(int i = 0; i < exps.length; i++){
            if(!exps[i].isComplete){
                answerList.add(exps[i].toString());
            }
        }
        
        String[] answer = new String[answerList.size()];
        for(int i = 0; i < answer.length; i++){
            answer[i] = answerList.get(i);
        }
        return answer;
    }
}