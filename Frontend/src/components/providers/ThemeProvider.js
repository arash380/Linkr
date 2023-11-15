import {
  createTheme,
  ThemeProvider as MuiThemeProvider,
} from "@mui/material/styles";

const colors = {
  primary: {
    main: "#3F252C",
    dark: "#321D23",
  },
  secondary: {
    main: "#40263E",
  },
};

const theme = createTheme({
  palette: {
    primary: {
      main: colors.primary.main,
      dark: colors.primary.dark,
    },
    secondary: {
      main: colors.secondary.main,
    },
  },
});

const ThemeProvider = (props) => {
  const { children } = props;
  return <MuiThemeProvider theme={theme}>{children}</MuiThemeProvider>;
};

export default ThemeProvider;
