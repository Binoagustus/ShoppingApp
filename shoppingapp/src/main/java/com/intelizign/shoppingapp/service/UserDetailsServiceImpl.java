package com.intelizign.shoppingapp.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.intelizign.shoppingapp.exception.ShoppingException;
import com.intelizign.shoppingapp.model.Customer;
import com.intelizign.shoppingapp.repository.CustomerRepository;
import com.intelizign.shoppingapp.request.LoginRequest;
import com.intelizign.shoppingapp.request.UserUpdateRequest;
import com.intelizign.shoppingapp.response.CustomLoginResponse;
import com.intelizign.shoppingapp.utility.JwtUtils;
import com.intelizign.shoppingapp.utility.UserDetailsImpl;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	CustomerRepository userRepo;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Customer user = userRepo.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with username " + username));
		return UserDetailsImpl.build(user);
	}

	public void saveUser(Customer user) {
		if (userRepo.findByUsername(user.getUsername()).isPresent()) {
			throw new ShoppingException("username already exists");
		}
		if (userRepo.findByEmail(user.getEmail()).isPresent()) {
			throw new ShoppingException("Mail Id already exists");
		}
		user.setPassword(encoder.encode(user.getPassword()));
		userRepo.save(user);
	}

	public CustomLoginResponse loginUser(LoginRequest request) {

		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return new CustomLoginResponse(userDetails.getUserId(), userDetails.getUsername(), userDetails.getEmail(),
				userDetails.getPassword(), jwtCookie, roles);

	}

	public void signoutUser(HttpServletRequest request, HttpServletResponse response) {
		ResponseCookie cookie;
		if (request.getHeader("Cookie") == null) {
			throw new ShoppingException("Signout User returning null");
		} else {
			cookie = jwtUtils.getCleanJwtCookie();
			response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
		}
	}

	public Customer updateUserInformation(UserUpdateRequest updateInfo, HttpServletRequest request) {
		String username = request.getUserPrincipal().getName();

		if (userRepo.findByUsername(username).isPresent()) {
			Optional<Customer> user = userRepo.findByUsername(username);
			if (updateInfo.getFirstName() != null) {
				user.get().setFirstName(updateInfo.getFirstName());
			}
			if (updateInfo.getLastName() != null) {
				user.get().setLastName(updateInfo.getLastName());
			}
			if (updateInfo.getAddress() != null) {
				user.get().setAddress(updateInfo.getAddress());
			}
			if (updateInfo.getMobilenumber() != null) {
				user.get().setMobilenumber(updateInfo.getMobilenumber());
			}
			userRepo.save(user.get());
			return user.get();
		} else
			throw new ShoppingException("User Name is returning null");
	}

	public void deleteCustomerById(Long userId) {
		if (userRepo.findById(userId).isPresent()) {
			userRepo.deleteById(userId);
		} else
			throw new ShoppingException("User returned null, could not be deleted");
	}

	public Page<Customer> viewAllCustomers(int pagenumber, int pagesize) {
		Pageable paging;
		if (userRepo.findAll() == null) {
			throw new ShoppingException("List of Customers is returning null");
		} else {
			paging = PageRequest.of(pagenumber, pagesize);
			return userRepo.findAll(paging);
		}
	}

	public String updateResetPasswordToken(String token, String username) {
		Optional<Customer> user = userRepo.findByUsername(username);
		if (user.isPresent()) {
			user.get().setPasswordResetToken(token);
			userRepo.save(user.get());
			return user.get().getPasswordResetToken();
		} else {
			throw new ShoppingException("Could not find any customer with the username " + username);
		}
	}

	public Customer resetpassword(String token, String newPassword) {
		Customer user = userRepo.findByPasswordResetToken(token).get();

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(newPassword);
		user.setPassword(encodedPassword);

		user.setPasswordResetToken(null);
		return userRepo.save(user);
	}
}
