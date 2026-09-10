-- 익명 댓글 저장소 (T2-4 크게 보기 전용)
-- Supabase 대시보드 → SQL Editor 에 그대로 붙여넣고 Run.
-- 그 다음 site/index.html 의 CMT 상수에 Project URL 과 anon(publishable) key 를 넣으면 켜진다.
-- service_role 키는 넣지 말 것 — 사이트는 정적이라 키가 그대로 노출된다.

create table if not exists public.comments (
  id          bigint generated always as identity primary key,
  solution    text        not null,          -- '{platform}/{problemId}#{author}[.variant]'
  nick        text,                          -- 비면 화면에서 '익명'
  body        text        not null,
  created_at  timestamptz not null default now()
);

create index if not exists comments_solution_idx
  on public.comments (solution, created_at);

alter table public.comments enable row level security;

-- 읽기: 누구나. 이 사이트는 로그인이 없다(비목표 2항).
drop policy if exists "anon read" on public.comments;
create policy "anon read" on public.comments
  for select to anon using (true);

-- 쓰기: 누구나 새 댓글만. 길이 제한을 DB 쪽에서도 건다(클라이언트만 믿지 않는다).
drop policy if exists "anon insert" on public.comments;
create policy "anon insert" on public.comments
  for insert to anon with check (
    char_length(solution) between 1 and 200
    and char_length(body) between 1 and 1000
    and (nick is null or char_length(nick) <= 20)
  );

-- update / delete 정책은 만들지 않는다 → RLS 기본값이 거부라 익명 사용자는 수정·삭제할 수 없다.
-- 스팸이 들어오면 대시보드 SQL Editor 에서 지운다:
--   delete from public.comments where id = 123;
