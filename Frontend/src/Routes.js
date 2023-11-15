import React from "react";
import {
  BrowserRouter as Router,
  Route,
  Routes as Switch,
  Navigate,
} from "react-router-dom";
import MainLayout from "./components/layouts/MainLayout/MainLayout";
import Home from "./components/routes/Home/Home";
import Projects from "./components/routes/Projects/Projects";
import ProjectDetails from "./components/routes/Projects/ProjectDetails";
import CreateProjectForm from "./components/routes/Projects/CreateProject";
import WorkPackages from "./components/routes/Projects/WorkPackages/WorkPackages";
import Profile from "./components/routes/Profile/Profile";
import ProtectedRoute from "./components/routing/ProtectedRoute";
import rp from "./components/routing/routePaths";
import Timesheets from "./components/routes/Timesheets/Timesheets";
import EditTimesheet from "./components/routes/Timesheets/EditTimesheet";
import Employees from "./components/routes/Employees/Employees";
import Login from "./components/routes/Login/Login";
import UpdatePassword from "./components/routes/Profile/UpdatePassword";
import { useUser } from "./contexts/userContext";
import WorkPackageDetails from "./components/routes/Projects/WorkPackages/WorkPackageDetails";
import Backup from "./components/routes/Backup/Backup";
import AppLoader from "./components/UI/loading/AppLoader/AppLoader";
import Reports from "./components/routes/Reports/Reports";

const Routes = () => {
  const { user, loading } = useUser();

  if (loading) return <AppLoader />;
  return (
    <Router>
      <Switch>
        <Route element={<ProtectedRoute reverse />}>
          <Route path={rp.login.path} element={<Login />} />
          <Route
            path={rp.wildCard.path}
            element={<Navigate to={rp.login.path} />}
          />
        </Route>

        <Route element={<ProtectedRoute />}>
          <Route element={<MainLayout />}>
            <Route path={rp.timesheets.path} element={<Timesheets />} />
            <Route path={rp.projectDetails.path} element={<ProjectDetails />} />
            <Route path={rp.projectReport.path} element={<Reports />} />
            <Route path={rp.editTimesheet.path} element={<EditTimesheet />} />
            <Route path={rp.projects.path} element={<Projects />} />
            <Route
              path={rp.createProjects.path}
              element={<CreateProjectForm />}
            />
            <Route path={rp.workPackages.path} element={<WorkPackages />} />
            <Route
              path={rp.workPackageDetails.path}
              element={<WorkPackageDetails />}
            />

            {!user?.isAdmin && (
              <Route path={rp.profile.path} element={<Profile />} />
            )}
            {!user?.isAdmin && (
              <Route
                path={rp.updatePassword.path}
                element={<UpdatePassword />}
              />
            )}
            {user?.isAdmin && (
              <Route path={rp.backup.path} element={<Backup />} />
            )}
            {(user?.isAdmin || user?.hrEmployee) && (
              <Route path={rp.employees.path} element={<Employees />} />
            )}
            <Route path={rp.home.path} element={<Home />} />
            <Route
              path={rp.wildCard.path}
              element={<Navigate to={rp.home.path} />}
            />
          </Route>
        </Route>
      </Switch>
    </Router>
  );
};

export default Routes;
