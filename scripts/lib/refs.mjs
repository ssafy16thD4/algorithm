// 외부 레퍼런스 저장소의 URL 조립. build-site 와 sync-references 가 같이 쓴다.
// 여기에 부수효과를 두지 말 것 — build-site 가 import 하는 순간 다운로드가 돌아버린다.

/** raw 파일 주소. 한글 경로가 있으므로 세그먼트 단위로 인코딩한다. */
export function rawUrl(provider, sourcePath) {
  const repo = provider.repo.replace(/^https:\/\/github\.com\//, '').replace(/\/$/, '');
  return `https://raw.githubusercontent.com/${repo}/${provider.pinnedCommit}/${encPath(sourcePath)}`;
}

/** 사람이 눌러서 볼 GitHub blob 주소. 커밋 SHA 로 고정한다. */
export function blobUrl(provider, sourcePath) {
  return `${provider.repo.replace(/\/$/, '')}/blob/${provider.pinnedCommit}/${encPath(sourcePath)}`;
}

const encPath = (p) => p.split('/').map(encodeURIComponent).join('/');
