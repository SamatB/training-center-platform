import api from "./axios";

export const getUsers = async ({
                                   page = 0,
                                   size = 10,
                                   role = "",
                                   sortBy = "createdAt",
                                   direction = "desc"
                               } = {}) => {
    const params = {
        page,
        size,
        sortBy,
        direction
    };

    if (role) {
        params.role = role;
    }

    const response = await api.get(
        "/api/auth/admin/users",
        { params }
    );

    return response.data;
};

export const changeUserRole = async (userId, role) => {
    const response = await api.patch(
        `/api/auth/admin/users/${userId}/role`,
        {
            role
        }
    );

    return response.data;
};

export const changeUserStatus = async (userId, enabled) => {
    const response = await api.patch(
        `/api/auth/admin/users/${userId}/status`,
        {
            enabled
        }
    );

    return response.data;
};

export const deleteUser = async (userId) => {
    await api.delete(
        `/api/auth/admin/users/${userId}`
    );
};

export const deleteAllStudents = async () => {
    const response = await api.delete(
        "/api/auth/admin/users/students"
    );

    return response.data;
};

export const getUserById = async (userId) => {
    const response = await api.get(
        `/api/auth/admin/users/${userId}`
    );

    return response.data;
};