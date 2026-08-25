// ref-title: [프로그래머스 / Level3] 불량 사용자 (Java)
// ref-url: https://velog.io/@pppp0722/프로그래머스-Level3-불량-사용자-Java
// ref-note: DFS 백트래킹으로 banned_id 순서대로 매칭 가능한 user_id를 하나씩 골라 HashSet에 담고, depth가 banned_id 개수에 도달하면 그 HashSet을 결과용 HashSet<HashSet<String>>에 저장해 중복 조합을 자동 제거한다. 저작권상 코드 원문은 저장하지 않으니 위 링크에서 직접 확인.
