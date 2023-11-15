import React from 'react';
import ReactDOM from 'react-dom';
import Profile from './../Profile'
import useAxiosGet from "../../../../hooks/useAxiosGet";
import { BrowserRouter  } from "react-router-dom";
import {setDefaultHeader} from "../../../../services/api/AxiosInstance";
import {UserProvider} from "../../../../contexts/userContext";
import { renderHook, act } from '@testing-library/react-hooks';
import {render, cleanup} from '@testing-library/react'
import '@testing-library/jest-dom'

afterEach(cleanup);
// seems like userProvider doesnt load in time, even with a default header and chanign to async
// it("renders Profile without crashing",  ()=> {
//     setDefaultHeader(
//         "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOjAsIm5hbWUiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiIsImV4cCI6MTg2NTI3MDY5MzUyM30=.tRD6FuS8ax8GeHnI7y05seSqLT6tQY1Xb6x3f8je9CQ"
//       );
//     const div = document.createElement("div");
    
//     console.log("yo ",<UserProvider></UserProvider>)
//    ReactDOM.render(<UserProvider> <BrowserRouter>  <Profile></Profile> </BrowserRouter> </UserProvider>, div)
// })

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

    
//    console.log(result.current.response);
  });
  
