import React, {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useState,
} from "react";
import jwtDecode from "jwt-decode";
import {
  DEFAULT_API as axios,
  setDefaultHeader,
} from "../services/api/AxiosInstance";
import { toast } from "react-toastify";

const TOKEN_KEY = "token_4911_linkr";
const ADMIN_SUB = 0;
const ADMIN_USER = {
  employeeID: 0,
  firstName: "Admin",
  lastName: "",
  userName: "Admin",
  isAdmin: true,
};

const UserContext = createContext();

export const useUser = () => useContext(UserContext);

export const UserProvider = ({ children }) => {
  const [user, setUserState] = useState();
  const [loading, setLoading] = useState(true);

  const fetchUserDetailsAndSetUser = useCallback(async (token) => {
    const decodedToken = jwtDecode(token);
    setDefaultHeader(token);
    localStorage.setItem(TOKEN_KEY, token);

    if (decodedToken.sub === ADMIN_SUB) {
      setUserState(ADMIN_USER);
    } else {
      const res = await axios.get(`/employee/${decodedToken.sub}`);

      if (!res.data.active) {
        localStorage.clear(TOKEN_KEY);
        return toast.error("Your account has been locked!");
      }

      setUserState({
        ...res.data,
        userName: decodedToken.name,
        isAdmin: false,
      });
    }
  }, []);

  useEffect(() => {
    (async () => {
      setLoading(true);
      let t = localStorage.getItem(TOKEN_KEY);
      if (t) {
        await fetchUserDetailsAndSetUser(t);
      }

      setLoading(false);
    })();
  }, [fetchUserDetailsAndSetUser]);

  const setUser = async (t) => {
    if (!t) return;
    setLoading(true);

    try {
      await fetchUserDetailsAndSetUser(t);
    } catch {
      // no user
    }

    setLoading(false);
  };

  const logout = () => {
    localStorage.removeItem(TOKEN_KEY);
    window.location.reload();
  };

  return (
    <UserContext.Provider value={{ user, setUser, loading, logout }}>
      {children}
    </UserContext.Provider>
  );
};
