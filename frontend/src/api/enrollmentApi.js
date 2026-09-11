import api from "./axios";

export const createEnrollment = async (data) => {
    const response = await api.post("/api/v1/enrollments", data);
    return response.data;
};

export const getEnrollments = async () => {
    const response = await api.get("/api/v1/enrollments");
    return response.data;
};

export const getEnrollmentById = async (id) => {
    const response = await api.get(`/api/v1/enrollments/${id}`);
    return response.data;
};

export const getEnrollmentsByUserId = async (userId) => {
    const response = await api.get(
        `/api/v1/enrollments/user/${userId}`
    );

    return response.data;
};

export const getEnrollmentsByCourseId = async (courseId) => {
    const response = await api.get(
        `/api/v1/enrollments/course/${courseId}`
    );

    return response.data;
};