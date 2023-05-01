package com.intelizign.shoppingapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.intelizign.shoppingapp.model.Customer;
import com.intelizign.shoppingapp.request.LoginRequest;
import com.intelizign.shoppingapp.request.UserUpdateRequest;
import com.intelizign.shoppingapp.response.CustomLoginResponse;
import com.intelizign.shoppingapp.response.ResponseHandler;
import com.intelizign.shoppingapp.response.UserLoginResponse;
import com.intelizign.shoppingapp.service.UserDetailsServiceImpl;

import net.bytebuddy.utility.RandomString;

@RestController
@RequestMapping("/api/user")
public class UserController {

	Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@PostMapping("/registeruser")
	public ResponseEntity<Object> registerUser(@RequestBody Customer user) {
		try {
			userDetailsService.saveUser(user);
			return ResponseHandler.generateResponse("User registered succesfully", true, HttpStatus.OK, user);
		} catch (Exception ex) {

			logger.error("Could not register " + ex.getMessage());
			return ResponseHandler.generateResponse("User was not registered", false, HttpStatus.OK, null);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<Object> authenticateUser(@RequestBody LoginRequest request, HttpServletResponse response) {
		try {
			CustomLoginResponse loginResponse = userDetailsService.loginUser(request);
			response.setHeader(HttpHeaders.SET_COOKIE, loginResponse.getJwtCookie().toString());

			return ResponseHandler.generateResponse("User Logged In succesfully", true, HttpStatus.OK,
					new UserLoginResponse(loginResponse.getUserId(), loginResponse.getUsername(),
							loginResponse.getEmail(), loginResponse.getRoles()));

		} catch (Exception ex) {
			logger.error("Could not Login User " + ex.getMessage());
			return ResponseHandler.generateResponse("User was not registered", false, HttpStatus.OK, null);
		}
	}

	@PostMapping("/forgot_password")
	public ResponseEntity<Object> processForgotPassword(@RequestParam String username) {
		String token = RandomString.make(30);
		try {
			String passwordResetToken = userDetailsService.updateResetPasswordToken(token, username);
			return ResponseHandler.generateResponse("You can now reset your password", true, HttpStatus.OK,
					passwordResetToken);

		} catch (Exception ex) {
			return ResponseHandler.generateResponse("Could not reset password", false, HttpStatus.OK, null);
		}
	}

	@PostMapping("/reset_password")
	public ResponseEntity<Object> updatePassword(@RequestParam String token, @RequestParam String newPassword) {
		try {
			Customer user = userDetailsService.resetpassword(token, newPassword);
			return ResponseHandler.generateResponse("Password updated succesfully", true, HttpStatus.OK, user);
		} catch (Exception ex) {
			logger.error("Could not reset password" + ex.getMessage());
			return ResponseHandler.generateResponse("Unable to reset password", false, HttpStatus.OK, null);
		}
	}

	@PutMapping("/update")
	public ResponseEntity<Object> updateCustomerInfo(@RequestBody UserUpdateRequest updateInfo,
			HttpServletRequest request) {
		try {
			Customer user = userDetailsService.updateUserInformation(updateInfo, request);
			return ResponseHandler.generateResponse("User details updated succesfully", true, HttpStatus.OK, user);
		} catch (Exception ex) {
			logger.error("Could not update user details" + ex.getMessage());
			return ResponseHandler.generateResponse("User Details could not be updated", false, HttpStatus.OK, null);
		}
	}

	@DeleteMapping("/delete/{userId}")
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<Object> deleteCustomerByUserId(@PathVariable Long userId) {
		try {
			userDetailsService.deleteCustomerById(userId);
			return ResponseHandler.generateResponse("User deleted sucessfully", true, HttpStatus.OK, "User deleted");
		} catch (Exception ex) {
			logger.error("Could not delete User" + ex.getMessage());
			return ResponseHandler.generateResponse("User could not be deleted", false, HttpStatus.OK, null);
		}
	}

	@PostMapping("/signout")
	public ResponseEntity<Object> logoutUser(HttpServletRequest request, HttpServletResponse response) {
		try {
			userDetailsService.signoutUser(request, response);
			return ResponseHandler.generateResponse("User Signed Out Succesfully", true, HttpStatus.OK,
					"SignOut Succesfull");
		} catch (Exception ex) {
			logger.error("Could not Logout User " + ex.getMessage());
			return ResponseHandler.generateResponse("Logout Failure", false, HttpStatus.OK, null);
		}
	}

	@GetMapping("/viewcustomers/{pagenumber}/{pagesize}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Object> viewAllUsers(@PathVariable int pagenumber, @PathVariable int pagesize) {
		try {
			Page<Customer> customers = userDetailsService.viewAllCustomers(pagenumber, pagesize);
			return ResponseHandler.generateResponse("List of Customers", true, HttpStatus.OK, customers);
		} catch (Exception ex) {
			logger.error("List of Customers could not be retrieved " + ex.getMessage());
			return ResponseHandler.generateResponse("List Of Customers could not be retrieved", false, HttpStatus.OK,null);
		}
	}
}
