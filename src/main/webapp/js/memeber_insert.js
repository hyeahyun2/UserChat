const form = document.member_insert;

function insert(){
  let id = form.userID.value;
  let pw = form.userPassword1.value;
  let pwCheck = form.userPassword2.value;
  let name = form.userName.value;
  let age = form.userAge.value;
  let email = form.userEmail.value;
  // console.log(id, pw, pwCheck, name, email);
  
  // 아이디 정규표현식
  let regExpIdFirst = /^[a-z|A-Z]/; // 첫글자 영문
  let regEpxIdNext = /^[a-z|A-Z|0-9]{4,12}$/; // 이후 글자 영or숫자 사용
  // 비밀번호 정규표현식
  let regExpPasswd = /^[0-9]{6,12}$/;
  // 이름 정규표현식
  let regExpName = /^[가-힣]{2,4}$/;
  // 이메일 정규표현식
  let regExpEmail = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
  
  // 아이디 유효성 검사
  if(id.length == 0){ // 미입력시
    alert("아이디를 입력해주세요.");
    form.userID.select();
    return;
  }
  else if(!regExpIdFirst.test(id)){
    alert("아이디는 영문자로 시작해야합니다.");
    form.userID.select();
    return;
  }
  else if(!regEpxIdNext.test(id)){
    // console.log("else if");
    alert("아이디는 영문자 또는 숫자를 사용하여 4자이상 12자 이하로 작성하세요.");
    form.userID.select();
    return;
  }
  // 비밀번호 유효성 검사
  if(pw.length == 0){ // 미입력시
    alert("비밀번호를 입력해주세요.");
    form.userPassword1.select();
    return;
  }
  else if(!regExpPasswd.test(pw)){
    alert("비밀번호는 숫자만을 이용해서 6자이상 12자 이하로 작성해주세요.");
    form.userPassword1.select();
    return;
  }
  // 비밀번호 확인
  if(pwCheck != pw){
    alert("비밀번호가 일치하지 않습니다. 다시 확인해주세요.");
    form.userPassword2.select();
    return;
  }
  // 이름 유효성 검사
  if(name.length == 0){ // 미입력시
    alert("이름을 입력해주세요.");
    form.userName.select();
    return;
  }
  else if(!regExpName.test(name)){
    alert("이름은 한글만을 이용해서 2자이상 4자이하로 입력해 주세요!");
    form.userName.select();
    return;
  }
  // 나이 유효성 검사
  if(age.length == 0){ // 미입력시
    alert("나이를 입력해주세요.");
    form.userAge.select();
    return;
  }
   // 이메일 유효성 검사
  if(email.length == 0){ // 미입력시
    alert("이메일을 입력해주세요.");
    form.userEmail.select();
    return;
  }
  if(!regExpEmail.test(email)){
    alert("이메일 형식으로 입력해주세요.");
    form.userEmail.select();
    return;
  }

  alert("회원가입에 성공했습니다!");
  form.submit();
}