package MHproject.DTO;



//1. UserDto.java - 회원 정보 DTO
public class UserDTO {
 private String userid;
 private String password;
 private String email;
 private String verification;
 
 // 기본 생성자
 public UserDTO() {}
 
 // 모든 필드 생성자
 public UserDTO(String userid, String password, String email, String verification) {
     this.userid = userid;
     this.password = password;
     this.email = email;
     this.verification = verification;
 }
 
 // Getter, Setter
 public String getUserid() {
     return userid;
 }
 
 public void setUserid(String userid) {
     this.userid = userid;
 }
 
 public String getPassword() {
     return password;
 }
 
 public void setPassword(String password) {
     this.password = password;
 }
 
 public String getEmail() {
     return email;
 }
 
 public void setEmail(String email) {
     this.email = email;
 }
 
 public String getVerification() {
     return verification;
 }
 
 public void setVerification(String verification) {
     this.verification = verification;
 }
 
 @Override
 public String toString() {
     return "UserDTO{" +
             "userid='" + userid + '\'' +
             ", password='" + password + '\'' +
             ", email='" + email + '\'' +
             ", verification='" + verification + '\'' +
             '}';
 }
}
