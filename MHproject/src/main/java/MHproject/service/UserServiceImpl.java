package MHproject.service;

import MHproject.service.UserService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import MHproject.DTO.UserDTO;
import MHproject.mapper.UserMapper;


@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserMapper userMapper;
	
	 public UserDTO login(String userid, String password) throws Exception {
	        UserDTO user = userMapper.findByUserid(userid);
	        
	        if (user != null && password.equals(user.getPassword())) {
	            // 비밀번호 제거
	            user.setPassword(null);
	            return user;
	        }
	        return null;
	    }

	 
	 
	 /**
	  * 이메일로 사용자 아이디 찾기
	  */
	 public String findUserIdByEmail(String email) {
	     try {
	         // 입력값 검증
	         if (email == null || email.trim().isEmpty()) {
	             System.out.println("이메일이 비어있습니다.");
	             return null;
	         }
	         
	         // 이메일 형식 검증
	         if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
	             System.out.println("잘못된 이메일 형식입니다: " + email);
	             return null;
	         }
	         
	         // 데이터베이스에서 이메일로 사용자 조회
	         String userId = userMapper.findUserIdByEmail(email); // 또는 userMapper.findUserIdByEmail(email);
	         
	         if (userId != null && !userId.trim().isEmpty()) {
	             System.out.println("이메일로 사용자 찾기 성공: " + email + " -> " + userId);
	             return userId;
	         } else {
	             System.out.println("해당 이메일로 등록된 사용자가 없습니다: " + email);
	             return null;
	         }
	         
	     } catch (Exception e) {
	         System.err.println("이메일로 사용자 찾기 중 오류 발생: " + e.getMessage());
	         // UserService.java에 추가해야 할 메서드들
	     }
		return email;
	     
	 }

	 /**
	  * 이메일로 사용자 아이디 찾기
	  */
	   public String findUserByEmail(String email) {
	     // 실제 구현에서는 데이터베이스에서 조회
	     // 예시 구현 (실제로는 DAO나 Repository를 통해 구현)
	     
	     // 임시 테스트용 데이터
	        	 try {
	    	         return userMapper.findUserIdByEmail(email);
	    	     } catch (Exception e) {
	    	         System.err.println("이메일로 사용자 찾기 실패: " + e.getMessage());
	    	         return null;
	    	     }
	     
	     /* 실제 구현 예시:
	     try {
	         return userDAO.findUserIdByEmail(email);
	     } catch (Exception e) {
	         System.err.println("이메일로 사용자 찾기 실패: " + e.getMessage());
	         return null;
	     }
	     */
	 }

	   /**
	     * 사용자 비밀번호 업데이트 (실제 데이터베이스 연동)
	     */
	    @Override
	    public boolean updatePassword(String userid, String newPassword) {
	        try {
	            System.out.println("=== updatePassword 호출 ===");
	            System.out.println("사용자 ID: " + userid);
	            System.out.println("새 비밀번호: " + newPassword);
	            
	            if (userid == null || userid.trim().isEmpty()) {
	                System.err.println("사용자 ID가 비어있습니다.");
	                return false;
	            }
	            
	            if (newPassword == null || newPassword.trim().isEmpty()) {
	                System.err.println("새 비밀번호가 비어있습니다.");
	                return false;
	            }
	            
				/*
				 * // 비밀번호 암호화 (선택사항 - 현재는 평문으로 저장) // String hashedPassword =
				 * passwordEncoder.encode(newPassword); String passwordToSave = newPassword;
				 * 
				 * // 데이터베이스 업데이트를 위한 파라미터 준비 Map<String, Object> params = new HashMap<>();
				 * params.put("userid", userid); params.put("password", passwordToSave);
				 */
	            
	            System.out.println("DB 업데이트 시작...");
	            
	            // 실제 데이터베이스 업데이트 실행
	            int updateCount = userMapper.updatePassword(userid, newPassword);
	            
	            System.out.println("DB 업데이트 결과: " + updateCount + "행 수정됨");
	            
	            if (updateCount > 0) {
	                System.out.println("비밀번호 업데이트 성공!");
	                
	                // 업데이트 확인을 위한 조회
					/*
					 * String updatedPassword = userMapper.getPasswordByUserId(userid);
					 * System.out.println("확인된 저장된 비밀번호: " + updatedPassword);
					 */
	                
	                return true;
	            } else {
	                System.err.println("비밀번호 업데이트 실패 - 해당 사용자가 존재하지 않음: " + userid);
	                return false;
	            }
	            
	        } catch (Exception e) {
	            System.err.println("updatePassword 오류: " + e.getMessage());
	            e.printStackTrace();
	            return false;
	        }
	    }




	 
}

	
	
	
	


