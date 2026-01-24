# Role Selection API Documentation

## Overview
The backend now supports user role selection functionality. Users can select their role after login, and the role is stored in the database and returned in authentication responses.

## User Roles
The system supports the following roles (defined in `UserRole` enum):
- `CANDIDATE` - Job seekers
- `RECRUITER` - Job recruiters
- `CAREER_COUNSELOR` - Career counselors
- `VERIFICATION_OFFICER` - Document verification officers
- `ADMIN` - System administrators

## API Endpoints

### 1. Get Current User Role
**GET** `/api/role/current`

Retrieves the current role of the authenticated user.

**Headers:**
```
Authorization: Bearer <access_token>
```

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Role retrieved successfully",
  "role": "CANDIDATE",
  "email": "user@example.com",
  "username": "a1b2c3d4e5f6g7h8"
}
```

**Response (404 Not Found):**
```json
{
  "success": false,
  "message": "User not found"
}
```

---

### 2. Update User Role
**PUT** `/api/role/update`

Updates the role of the authenticated user.

**Headers:**
```
Authorization: Bearer <access_token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "role": "CANDIDATE"
}
```

**Valid role values:**
- `CANDIDATE`
- `RECRUITER`
- `CAREER_COUNSELOR`
- `VERIFICATION_OFFICER`
- `ADMIN`

**Response (200 OK):**
```json
{
  "success": true,
  "message": "Role updated successfully",
  "role": "CANDIDATE",
  "email": "user@example.com",
  "username": "a1b2c3d4e5f6g7h8"
}
```

**Response (400 Bad Request):**
```json
{
  "success": false,
  "message": "Role cannot be null"
}
```

---

## Updated Authentication Response

The `AuthResponse` now includes a `role` field:

**Signup/Login Response:**
```json
{
  "success": true,
  "message": "User registered successfully",
  "username": "a1b2c3d4e5f6g7h8",
  "email": "user@example.com",
  "role": null,
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Note:** The `role` field will be `null` for newly registered users until they select a role.

---

## Frontend Integration Guide

### Step 1: Update Login/Signup Flow

After successful login/signup, check if the user has a role:

```typescript
// After login/signup API call
const response = await fetch('/api/auth/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ email, password })
});

const data = await response.json();

if (data.success) {
  // Save token
  localStorage.setItem('token', data.accessToken);
  localStorage.setItem('user', JSON.stringify({
    email: data.email,
    username: data.username,
    role: data.role // Can be null
  }));

  // Check if role is set
  if (data.role) {
    // Redirect to appropriate dashboard
    router.push(getDashboardPath(data.role));
  } else {
    // Redirect to role selection
    router.push('/select-role');
  }
}
```

### Step 2: Update Role Selection Pages

#### `/select-role` page - Update role when candidate is selected:

```typescript
const handleRoleSelection = async (roleType: 'candidate' | 'recruitment') => {
  if (isLoading) return;
  
  setIsLoading(true);

  try {
    const token = localStorage.getItem('token');
    
    if (roleType === 'candidate') {
      // Update role to CANDIDATE
      const response = await fetch('/api/role/update', {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({ role: 'CANDIDATE' })
      });

      const data = await response.json();
      
      if (data.success) {
        // Update local storage
        const user = JSON.parse(localStorage.getItem('user') || '{}');
        user.role = data.role;
        localStorage.setItem('user', JSON.stringify(user));
        
        // Redirect to candidate profile
        router.push('/dashboard/candidate/profile');
      } else {
        throw new Error(data.message);
      }
    } else {
      // Redirect to recruitment role selection
      router.push('/select-recruitment-role');
    }
  } catch (error) {
    console.error('Role selection error:', error);
    // Show error message
  } finally {
    setIsLoading(false);
  }
};
```

#### `/select-recruitment-role` page - Update role when recruiter/counselor is selected:

```typescript
const handleRoleSelection = async (role: 'RECRUITER' | 'CAREER_COUNSELOR') => {
  if (isLoading) return;
  
  setIsLoading(true);

  try {
    const token = localStorage.getItem('token');
    
    // Update role
    const response = await fetch('/api/role/update', {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify({ role })
    });

    const data = await response.json();
    
    if (data.success) {
      // Update local storage
      const user = JSON.parse(localStorage.getItem('user') || '{}');
      user.role = data.role;
      localStorage.setItem('user', JSON.stringify(user));
      
      // Redirect to appropriate dashboard
      const dashboardPath = getDashboardPath(data.role);
      router.push(dashboardPath);
    } else {
      throw new Error(data.message);
    }
  } catch (error) {
    console.error('Role selection error:', error);
    // Show error message
  } finally {
    setIsLoading(false);
  }
};
```

### Step 3: Helper Function for Role Mapping

Create a helper function to map backend roles to frontend roles:

```typescript
// lib/roleMapper.ts
export function mapBackendRoleToFrontend(backendRole: string | null): UserRole | null {
  if (!backendRole) return null;
  
  const roleMap: Record<string, UserRole> = {
    'CANDIDATE': 'candidate',
    'RECRUITER': 'recruiter',
    'CAREER_COUNSELOR': 'career-counselor',
    'VERIFICATION_OFFICER': 'verification-officer',
    'ADMIN': 'admin'
  };
  
  return roleMap[backendRole] || null;
}

export function mapFrontendRoleToBackend(frontendRole: UserRole): string {
  const roleMap: Record<UserRole, string> = {
    'candidate': 'CANDIDATE',
    'recruiter': 'RECRUITER',
    'career-counselor': 'CAREER_COUNSELOR',
    'verification-officer': 'VERIFICATION_OFFICER',
    'admin': 'ADMIN'
  };
  
  return roleMap[frontendRole];
}
```

---

## Database Schema

The `users` table now includes a `role` column:

```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(60) NOT NULL,
    username VARCHAR(16) UNIQUE NOT NULL,
    role VARCHAR(50),  -- Can be NULL initially
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
```

**Note:** The `role` column is nullable, allowing users to register without a role and select it later.

---

## Security

- All role endpoints require JWT authentication
- Users can only update their own role
- Role validation ensures only valid enum values are accepted
- The role is stored in the database and included in JWT tokens (via user claims)

---

## Testing

### Test with cURL:

1. **Login:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"password123"}'
```

2. **Get Current Role:**
```bash
curl -X GET http://localhost:8080/api/role/current \
  -H "Authorization: Bearer <access_token>"
```

3. **Update Role:**
```bash
curl -X PUT http://localhost:8080/api/role/update \
  -H "Authorization: Bearer <access_token>" \
  -H "Content-Type: application/json" \
  -d '{"role":"CANDIDATE"}'
```

---

## Swagger Documentation

All endpoints are documented in Swagger UI:
- Access at: `http://localhost:8080/swagger-ui.html`
- Look for the "Role Management" tag

---

## Notes

- Users start with `role = null` after registration
- Role must be selected before accessing role-specific dashboards
- Role can be updated at any time by the user
- The role is persisted in the database and returned in all user-related responses
