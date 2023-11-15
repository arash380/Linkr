import React from 'react';
import ReactDOM from 'react-dom';
import EmployeeForm, { EMP_FORM_MODES as modes } from "./../EmployeeForm";
import Employees from './../Employees'
import useAxiosGet from "../../../../hooks/useAxiosGet";
import {setDefaultHeader} from "../../../../services/api/AxiosInstance";
import {render, cleanup} from '@testing-library/react'

import '@testing-library/jest-dom'
import { renderHook, act } from '@testing-library/react-hooks';



afterEach(cleanup);

// it("renders Employees without crashing", ()=> {
//     const div = document.createElement("div");
//     ReactDOM.render(<Employees></Employees>, div)
// })


it("Correctly gets the employees", async () => {
    setDefaultHeader(
      "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOjAsIm5hbWUiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiIsImV4cCI6MTg2NTI3MDY5MzUyM30=.tRD6FuS8ax8GeHnI7y05seSqLT6tQY1Xb6x3f8je9CQ"
    );
    const { result, waitForValueToChange } = renderHook(() =>
      useAxiosGet("/employee")
    );
  
    await waitForValueToChange(() => {
      return result.current.isLoading;
    });
  
    console.log(result.current.response);
  });

  it("Verifying data is correct", async () => {
    setDefaultHeader(
      "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOjAsIm5hbWUiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiIsImV4cCI6MTg2NTI3MDY5MzUyM30=.tRD6FuS8ax8GeHnI7y05seSqLT6tQY1Xb6x3f8je9CQ"
    );
    const { result, waitForValueToChange } = renderHook(() =>
      useAxiosGet("/employee")
    );
  
    await waitForValueToChange(() => {
      return result.current.isLoading;
    });
  
    expect(result.current.response[0].firstName).toEqual("Joe");
  });

