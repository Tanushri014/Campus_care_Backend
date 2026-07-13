package campus_care.service.adminService;

import campus_care.dto.AdminProfileDto;

public interface AdminService {

    AdminProfileDto getCurrentAdmin(String email);

}