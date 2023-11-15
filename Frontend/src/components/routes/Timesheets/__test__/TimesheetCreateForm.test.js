import React from 'react';
import ReactDOM from 'react-dom';
import TimesheetCreateForm from '../TimesheetCreateForm'
import useAxiosGet from "../../../../hooks/useAxiosGet";
import {setDefaultHeader} from "../../../../services/api/AxiosInstance";
import { renderHook, act } from '@testing-library/react-hooks';
import {render, cleanup, screen} from '@testing-library/react'
import {UserProvider, useUser } from "../../../../contexts/userContext";
import AdapterDateFns from "@mui/lab/AdapterDateFns";
import LocalizationProvider from "@mui/lab/LocalizationProvider";
import { BrowserRouter  } from "react-router-dom";
import userEvent from '@testing-library/user-event'
import '@testing-library/jest-dom'

afterEach(cleanup);

it("renders CreateTimesheet without crashing", ()=> {
    const div = document.createElement("div");
    ReactDOM.render(   <LocalizationProvider dateAdapter={AdapterDateFns}><UserProvider><BrowserRouter><TimesheetCreateForm></TimesheetCreateForm></BrowserRouter></UserProvider></LocalizationProvider>, div)
})


it("fetches data", async () => {
    setDefaultHeader(
      "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOjAsIm5hbWUiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiIsImV4cCI6MTg2NTI3MDY5MzUyM30=.tRD6FuS8ax8GeHnI7y05seSqLT6tQY1Xb6x3f8je9CQ"
    );
    const { result, waitForValueToChange } = renderHook(() =>
      useAxiosGet("/timesheet")
    );
  
    await waitForValueToChange(() => {
      return result.current.isLoading;
    });
  
    console.log(result.current.response);
  });

  it("Choose a date", async ()=> {
    // const {result} = renderHook(useAxiosGet('/employee'));
    // console.log(result);
    global.fetch = jest.fn()
  
    await act(async () => renderHook(() => useAxiosGet("/timesheet")))
  
    render( <LocalizationProvider dateAdapter={AdapterDateFns}><UserProvider><BrowserRouter><TimesheetCreateForm></TimesheetCreateForm></BrowserRouter></UserProvider></LocalizationProvider>)


    expect(screen.getByRole('button', {
        name: /apr 30, 2022/i
      })).toHaveValue

     
  })

  

