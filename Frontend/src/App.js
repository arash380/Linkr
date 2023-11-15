import Routes from "./Routes";
import CssBaseline from "@mui/material/CssBaseline";
import { StyledEngineProvider } from "@mui/material";
import ThemeProvider from "./components/providers/ThemeProvider";
import useColors from "./hooks/useColors";
import { UserProvider } from "./contexts/userContext";
import { ToastContainer } from "react-toastify";
import AdapterDateFns from "@mui/lab/AdapterDateFns";
import LocalizationProvider from "@mui/lab/LocalizationProvider";
import "react-toastify/dist/ReactToastify.css";

const App = () => {
  useColors();

  return (
    <LocalizationProvider dateAdapter={AdapterDateFns}>
      <ThemeProvider>
        <CssBaseline />
        <StyledEngineProvider injectFirst>
          <UserProvider>
            <ToastContainer />
            <Routes />
          </UserProvider>
        </StyledEngineProvider>
      </ThemeProvider>
    </LocalizationProvider>
  );
};

export default App;
