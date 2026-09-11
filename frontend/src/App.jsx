import {Navigate, Route, Routes} from "react-router-dom";

import LoginPage from "./pages/auth/LoginPage";
import RegisterPage from "./pages/auth/RegisterPage";
import HomePage from "./pages/HomePage";

import AdminHomePage from "./pages/admin/AdminHomePage";
import AdminUsersPage from "./pages/admin/AdminUsersPage";
import AdminCoursesPage from "./pages/admin/AdminCoursesPage";
import AdminCreateCoursePage from "./pages/admin/AdminCreateCoursePage";
import AdminEditCoursePage from "./pages/admin/AdminEditCoursePage";
import AdminEnrollmentsPage from "./pages/admin/AdminEnrollmentsPage";
import AdminPaymentsPage from "./pages/admin/AdminPaymentsPage";
import AdminDocumentsPage from "./pages/admin/AdminDocumentsPage";

import TeacherHomePage from "./pages/teacher/TeacherHomePage";
import TeacherCoursesPage from "./pages/teacher/TeacherCoursesPage";
import TeacherCourseDetailsPage from "./pages/teacher/TeacherCourseDetailsPage";

import StudentHomePage from "./pages/student/StudentHomePage";
import StudentProfilePage from "./pages/student/StudentProfilePage";
import StudentCoursesPage from "./pages/student/StudentCoursesPage";
import StudentCourseDetailsPage from "./pages/student/StudentCourseDetailsPage";
import StudentEnrollmentsPage from "./pages/student/StudentEnrollmentsPage";
import StudentPaymentsPage from "./pages/student/StudentPaymentsPage";
import StudentDocumentsPage from "./pages/student/StudentDocumentsPage";

import ProtectedRoute from "./auth/ProtectedRoute";
import RoleRoute from "./auth/RoleRoute";
import Layout from "./components/Layout";

function App() {
    return (
        <Routes>
            <Route path="/login" element={<LoginPage/>}/>
            <Route path="/register" element={<RegisterPage/>}/>

            <Route element={<ProtectedRoute/>}>
                <Route element={<Layout/>}>
                    <Route path="/" element={<HomePage/>}/>

                    <Route
                        element={
                            <RoleRoute
                                allowedRoles={["STUDENT"]}
                            />
                        }
                    >
                        <Route
                            path="/student"
                            element={<StudentHomePage/>}
                        />

                        <Route
                            path="/student/profile"
                            element={<StudentProfilePage/>}
                        />

                        <Route
                            path="/student/courses"
                            element={<StudentCoursesPage/>}
                        />

                        <Route
                            path="/student/courses/:id"
                            element={<StudentCourseDetailsPage/>}
                        />

                        <Route
                            path="/student/enrollments"
                            element={<StudentEnrollmentsPage/>}
                        />

                        <Route
                            path="/student/payments"
                            element={<StudentPaymentsPage/>}
                        />

                        <Route
                            path="/student/documents"
                            element={<StudentDocumentsPage/>}
                        />

                    </Route>

                    <Route
                        element={
                            <RoleRoute
                                allowedRoles={["TEACHER"]}
                            />
                        }
                    >
                        <Route
                            path="/teacher"
                            element={<TeacherHomePage/>}
                        />

                        <Route
                            path="/teacher/courses"
                            element={<TeacherCoursesPage />}
                        />

                        <Route
                            path="/teacher/courses/:id"
                            element={<TeacherCourseDetailsPage />}
                        />
                    </Route>

                    <Route
                        element={
                            <RoleRoute
                                allowedRoles={["ADMIN"]}
                            />
                        }
                    >
                        <Route
                            path="/admin"
                            element={<AdminHomePage/>}
                        />

                        <Route
                            path="/admin/users"
                            element={<AdminUsersPage/>}
                        />

                        <Route
                            path="/admin/courses"
                            element={<AdminCoursesPage/>}
                        />

                        <Route
                            path="/admin/courses/create"
                            element={<AdminCreateCoursePage/>}
                        />

                        <Route
                            path="/admin/courses/:id/edit"
                            element={<AdminEditCoursePage/>}
                        />

                        <Route
                            path="/admin/enrollments"
                            element={<AdminEnrollmentsPage />}
                        />

                        <Route
                            path="/admin/payments"
                            element={<AdminPaymentsPage />}
                        />

                        <Route
                            path="/admin/documents"
                            element={<AdminDocumentsPage />}
                        />
                    </Route>
                </Route>
            </Route>

            <Route
                path="*"
                element={<Navigate to="/" replace/>}
            />
        </Routes>
    );
}

export default App;