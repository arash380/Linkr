import { Navigate, Outlet } from "react-router-dom";
import { useUser } from "../../contexts/userContext";
import AppLoader from "../UI/loading/AppLoader/AppLoader";
import rp from "./routePaths";

const ProtectedRoute = ({ reverse }) => {
  const { user: authenticated, loading } = useUser();

  if (loading) return <AppLoader />;

  if (reverse && authenticated) {
    return <Navigate to={rp.home.path} />;
  }

  if (!reverse && !authenticated) {
    return <Navigate to={rp.login.path} />;
  }

  return <Outlet />;
};

export default ProtectedRoute;
