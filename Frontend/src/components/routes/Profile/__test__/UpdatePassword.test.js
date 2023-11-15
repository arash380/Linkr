import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter  } from "react-router-dom";
import UpdatePassword from './../UpdatePassword'
import useAxiosGet from "../../../../hooks/useAxiosGet";
import {setDefaultHeader} from "../../../../services/api/AxiosInstance";
import { renderHook, act } from '@testing-library/react-hooks';
import {render, cleanup} from '@testing-library/react'
import { UserProvider, useUser } from "../../../../contexts/userContext";
import '@testing-library/jest-dom'
//import App from "../../../../App"
import { shallow } from 'enzyme';

afterEach(cleanup);

it("renders UpdatePassword without crashing", ()=> {
    setDefaultHeader(
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOjAsIm5hbWUiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiIsImV4cCI6MTg2NTI3MDY5MzUyM30=.tRD6FuS8ax8GeHnI7y05seSqLT6tQY1Xb6x3f8je9CQ"
      );
    const div = document.createElement("div");
    ReactDOM.render(<BrowserRouter><UserProvider> <UpdatePassword></UpdatePassword></UserProvider></BrowserRouter>, div)
})

it("fetches data", async () => {
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

  
  
