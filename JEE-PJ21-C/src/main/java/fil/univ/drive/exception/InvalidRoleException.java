package fil.univ.drive.exception;

import fil.univ.drive.service.user.Role;

public class InvalidRoleException extends RuntimeException {
	public Role role;

	public InvalidRoleException(String message, Role role) {
		super(message);
		this.role = role;
	}
}
