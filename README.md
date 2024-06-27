# 컴퓨터 공학 종합 설계 - 프로젝트 마치
## Bad Comment Filter - Server

### 기능 요구 사항

- [ ] 클라이언트로부터의 댓글 텍스트 해싱(SHA-256)
- [ ] 해싱값을 키로 redis에 캐싱
- [ ] 캐시미스시 모델서버로 fallback, 이후 판독결과를 redis에 캐싱
- [ ] 클라이언트로부터 댓글 뭉치 json을 수신
- [ ] 댓글 판독 결과는 개별적으로 클라이언트에 이벤트로 송신

___

## 사용 기술 스택 

- Spring Webflux
- Spring data redis reactive