    @Transactional
    public void deactivateCompany(Long companyId) {

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Company not found with id: " + companyId
                        )
                );

        company.setIsActive(false);

        company.getUsers().forEach(user -> user.setIsActive(false));

    }