import React from 'react';
import ReactDOM from 'react-dom';
import TimesheetRowForm from './../../TimesheetRows/TimesheetRowForm'
import useAxiosGet from "../../../../../hooks/useAxiosGet";
import {setDefaultHeader} from "../../../../../services/api/AxiosInstance";
import { BrowserRouter  } from "react-router-dom";
import { renderHook, act } from '@testing-library/react-hooks';
import {render, cleanup} from '@testing-library/react'
import { UserProvider, userProvider, useUser } from "../../../../../contexts/userContext";
import '@testing-library/jest-dom'

afterEach(cleanup);
//
// it("renders TimesheetRowForm without crashing", ()=> {
//     const div = document.createElement("div");
//     ReactDOM.render(<UserProvider><BrowserRouter><TimesheetRowForm></TimesheetRowForm></BrowserRouter></UserProvider>, div)
// })


it("fetches data", async () => {
    setDefaultHeader(
      "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOjAsIm5hbWUiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiIsImV4cCI6MTg2NTI3MDY5MzUyM30=.tRD6FuS8ax8GeHnI7y05seSqLT6tQY1Xb6x3f8je9CQ"
    );
    const { result, waitForValueToChange } = renderHook(() =>
      useAxiosGet("/timesheet-row")
    );
  
    await waitForValueToChange(() => {
      return result.current.isLoading;
    });
  
    console.log(result.current.response);
  });
  