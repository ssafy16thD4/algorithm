/*
전략
- 많이 재생된 장르 > 장르 내 많이 재생된 노래 > 같은 횟수 내 고유 번호가 낮은 노래
- 중복 없는 장르명 리스트 생성
- PQ[장르길이] 로해서 각 장르에 각 노래를 넣음
- 해시맵으로 각 노래를 넣을 때 장르 : 총재생수 갱신
- Song 클래스로 idx랑 play를 멤버변수로 해서 둘을 비교하도록 오버라이딩
    - 정렬 순서는 재생 수 많은 순. 같으면 고유 번호 낮은 순
- 장르명 리스트 순회로 해시맵에서 재생수 순위대로 인덱스를 배열에 저장
- 앞에서부터 1등 재생수의 인덱스로 해당 장르의 PQ에서 2개 꺼내서 결과 배열에 저장
- 2등 재생수의 인덱스로 해당 장르의 PQ에서 2개 꺼내서 결과 배열에 저장.. 반복
    - 결과 배열은 리스트로 먼저 하고 추후 배열로 변환
*/

import java.util.*;

class Solution {
    class Song implements Comparable<Song> {
        int idx;
        int play;
        
        Song(int idx, int play){
            this.idx = idx;
            this.play = play;
        }
        
        @Override
        public int compareTo(Song s){
            int comp = Integer.compare(s.play, this.play);
            if(comp != 0){
                return comp;
            }
             
            return Integer.compare(this.idx, s.idx);
        }
    }
    
    public int[] solution(String[] genres, int[] plays) {
        List<Integer> resultList = new ArrayList<>();
        // 중복 없는 장르명 리스트 생성
        List<String> genreList = new ArrayList<>();
        for(String g : genres){
            if(!genreList.contains(g)) {
                genreList.add(g);
            }
        }
        
        // 각 장르의 노래를 담는 우선순위 큐(재생 많은 순 -> 고유번호 낮은 순)
        // 각 장르의 총 재생 수를 담는 맵
        PriorityQueue<Song>[] playPQ = new PriorityQueue[genreList.size()];
        Map<String, Integer> genreMap = new HashMap<>();
        for(int i = 0; i < genreList.size(); i++){
            playPQ[i] = new PriorityQueue<>();
        }
        for(int i = 0; i < genres.length; i++){
            int idx = genreList.indexOf(genres[i]);
            int play = plays[i];
            playPQ[idx].add(new Song(i, play));
            genreMap.compute(genreList.get(idx), (k, v) -> (v == null) ? play : v + play);
        }
        
        // 장르의 총 재생수 순서대로 정렬시킨 엔트리 리스트를 만듬
        List<Map.Entry<String, Integer>> rankList = new ArrayList<>(genreMap.entrySet());
        rankList.sort(Map.Entry.<String, Integer>comparingByValue().reversed());
        
        // 총 재생수 순서대로 각 장르에서 2개씩 꺼냄
        // 만약 1개만 있는 장르라면 1개 꺼냄
        for(Map.Entry e : rankList){
            int idx = genreList.indexOf(e.getKey());
            System.out.println(genreList.get(idx));
            for(int i = 0; i < 2; i++){
                if(playPQ[idx].size() == 0) break;
                Song curr = playPQ[idx].poll();
                resultList.add(curr.idx);
            }
        }
        
        // 결과 반환
        int[] answer = new int[resultList.size()];
        for(int i = 0; i < resultList.size(); i++){
            answer[i] = resultList.get(i);
        }
        return answer;
    }
}