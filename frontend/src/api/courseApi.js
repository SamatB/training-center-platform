import api from "./axios";

export const getCourses = async () => {
    const response = await api.get("/api/v1/courses");
    return response.data;
};

export const getCourseById = async (id) => {
    const response = await api.get(`/api/v1/courses/${id}`);
    return response.data;
};

export const createCourse = async (data) => {
    const response = await api.post("/api/v1/courses", data);
    return response.data;
};

export const updateCourse = async (id, data) => {
    const response = await api.put(`/api/v1/courses/${id}`, data);
    return response.data;
};

export const deleteCourse = async (id) => {
    await api.delete(`/api/v1/courses/${id}`);
};

export const getCoursesByTeacherId = async (teacherId) => {
    const response = await api.get(
        `/api/v1/courses/teacher/${teacherId}`
    );

    return response.data;
};