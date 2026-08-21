package com.acsousa.gerenciador_de_rotinas.factories;

import com.acsousa.gerenciador_de_rotinas.domain.role.models.RoleModel;
import com.acsousa.gerenciador_de_rotinas.domain.role.records.RoleResponseRecord;

public class RoleFactory {

    public static RoleModel createRoleAdmin() {
        RoleModel roleModel = new RoleModel();

        roleModel.setId(1L);
        roleModel.setAuthority("ROLE_ADMIN");
        roleModel.setDescription("Administrador");

        return roleModel;
    }

    public static RoleResponseRecord createRoleAdminResponseRecord() {
        return RoleResponseRecord.fromEntity(createRoleAdmin());
    }
}
