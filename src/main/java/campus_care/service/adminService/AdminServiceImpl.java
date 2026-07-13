package campus_care.service.adminService;

import campus_care.dto.AdminProfileDto;
import campus_care.entity.Admin;
import campus_care.exception.ResourceNotFoundException;
import campus_care.repository.AdminRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    @Override
    public AdminProfileDto getCurrentAdmin(
            String email
    ) {

        log.info("Fetching profile for admin '{}'.", email);

        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> {

                    log.warn(
                            "Admin not found with email '{}'.",
                            email
                    );

                    return new ResourceNotFoundException(
                            "Admin not found"
                    );
                });

        log.info(
                "Profile fetched successfully for admin '{}'.",
                email
        );

        return AdminProfileDto.builder()
                .id(admin.getId())
                .email(admin.getEmail())
                .category(admin.getCategory().name())
                .build();
    }
}