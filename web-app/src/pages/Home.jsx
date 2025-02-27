import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Box, Card, CircularProgress, Typography } from "@mui/material";
import { getMyInfo } from "../services/userService";
import { isAuthenticated } from "../services/authenticationService";
import Scene from "./Scene";
import { Link } from "react-router-dom";

export default function Home() {
  const navigate = useNavigate();
  const [userDetails, setUserDetails] = useState({});

  const getUserDetails = async () => {
    try {
      const response = await getMyInfo();
      const data = response.data;

      console.log(data);

      setUserDetails(data.result);
    } catch (error) {}
  };

  useEffect(() => {
    if (!isAuthenticated()) {
      navigate("/login");
    } else {
      getUserDetails();
    }
  }, [navigate]);

  return (
    <Scene>
      {userDetails ? (
        <Card
          sx={{
            minWidth: 350,
            maxWidth: 500,
            boxShadow: 3,
            borderRadius: 2,
            padding: 4,
          }}
        >
          <Box
            sx={{
              display: "flex",
              flexDirection: "column",
              alignItems: "flex-start",
              width: "100%",
              gap: "10px",
            }}
          >
            <Typography sx={{ fontSize: 18, mb: "40px" }}>
              Welcome back to Devteria, {userDetails.username} !
            </Typography>
            {[
              { label: "User Id", value: userDetails.id },
              { label: "First Name", value: userDetails.firstName },
              { label: "Last Name", value: userDetails.lastName },
              { label: "Date of birth", value: userDetails.dob },
            ].map((item) => (
              <Box
                key={item.label}
                sx={{
                  display: "flex",
                  flexDirection: "row",
                  justifyContent: "space-between",
                  alignItems: "flex-start",
                  width: "100%",
                }}
              >
                <Typography sx={{ fontSize: 14, fontWeight: 600 }}>
                  {item.label}
                </Typography>
                <Typography sx={{ fontSize: 14 }}>{item.value}</Typography>
              </Box>
            ))}
          </Box>
        </Card>
      ) : (
        <Box
          sx={{
            display: "flex",
            flexDirection: "column",
            gap: "30px",
            justifyContent: "center",
            alignItems: "center",
            height: "100vh",
          }}
        >
          <CircularProgress></CircularProgress>
          <Typography>Loading ...</Typography>
        </Box>
      )}
      <Link to="/chat">
        <button>Go to Chat</button>
      </Link>
    </Scene>
  );
}
