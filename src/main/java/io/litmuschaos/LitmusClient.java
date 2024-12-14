package io.litmuschaos;

import com.google.gson.reflect.TypeToken;
import com.jayway.jsonpath.TypeRef;
import com.netflix.graphql.dgs.client.GraphQLResponse;
import com.netflix.graphql.dgs.client.codegen.GraphQLQueryRequest;
import io.litmuschaos.exception.LitmusApiException;
import io.litmuschaos.generated.client.*;
import io.litmuschaos.generated.types.*;
import io.litmuschaos.graphql.LitmusGraphQLClient;
import io.litmuschaos.http.LitmusHttpClient;
import io.litmuschaos.request.*;
import io.litmuschaos.response.*;
import okhttp3.OkHttpClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LitmusClient implements AutoCloseable {

    private String token;
    private final LitmusHttpClient httpClient;
    private final LitmusGraphQLClient graphQLClient;

    public LitmusClient(String host, String token) {
        String sanitizedHost = sanitizeURL(host);
        OkHttpClient okHttpClient = new OkHttpClient();
        this.token = token;
        this.httpClient = new LitmusHttpClient(okHttpClient, sanitizedHost + "/auth");
        this.graphQLClient = new LitmusGraphQLClient(okHttpClient, sanitizedHost + "/api/query", this.token);
    }

    @Override
    public void close() throws Exception {
        this.httpClient.close();
    }

    // User
//    public LoginResponse authenticate(LoginRequest request)
//            throws IOException, LitmusApiException {
//        LoginResponse response = httpClient.post("/login", request, LoginResponse.class);
//        this.token = response.getAccessToken();
//        return response;
//    }

//    public CommonResponse logout() throws IOException, LitmusApiException {
//        CommonResponse commonResponse = httpClient.post("/logout", token, CommonResponse.class);
//        this.token = "";
//        return commonResponse;
//    }

    public ListTokensResponse getTokens(String userId) throws IOException, LitmusApiException {
        return httpClient.get("/token/" + userId, token, ListTokensResponse.class);
    }

    public TokenCreateResponse createToken(TokenCreateRequest request) throws IOException, LitmusApiException {
        return httpClient.post("/create_token", token, request, TokenCreateResponse.class);
    }

    public CommonResponse deleteToken(TokenDeleteRequest request) throws IOException, LitmusApiException {
        return httpClient.post("/remove_token", token, request, CommonResponse.class);
    }

    public UserResponse getUser(String userId) throws IOException, LitmusApiException {
        return httpClient.get("/get_user/" + userId, token, UserResponse.class);
    }

    public List<UserResponse> getUsers() throws IOException, LitmusApiException {
        TypeToken<List<UserResponse>> typeToken = new TypeToken<>() {
        };
        return httpClient.get("/users", token, typeToken);
    }

    public PasswordUpdateResponse updatePassword(PasswordUpdateRequest request) throws IOException, LitmusApiException {
        return httpClient.post("/update/password", token, request, PasswordUpdateResponse.class);
    }

    public UserResponse createUser(UserCreateRequest request) throws IOException, LitmusApiException {
        return httpClient.post("/create_user", token, request, UserResponse.class);
    }

    public CommonResponse resetPassword(PasswordResetRequest request) throws IOException, LitmusApiException {
        return httpClient.post("/reset/password", token, request, CommonResponse.class);
    }

    public CommonResponse updateUserDetails(UserDetailsUpdateRequest request) throws IOException, LitmusApiException {
        return httpClient.post("/update/details", token, request, CommonResponse.class);
    }

    public CommonResponse updateUserState(UserStateUpdateRequest request) throws IOException, LitmusApiException {
        return httpClient.post("/update/state", token, request, CommonResponse.class);
    }

    // Capabilities
    public CapabilityResponse capabilities() throws IOException, LitmusApiException {
        return httpClient.get("/capabilities", CapabilityResponse.class);
    }

    // Project
    public ListProjectsResponse listProjects(ListProjectRequest request)
            throws IOException, LitmusApiException {
        Map<String, String> requestParam = new HashMap<>();
        requestParam.put("page", String.valueOf(request.getPage()));
        requestParam.put("limit", String.valueOf(request.getLimit()));
        requestParam.put("sortField", request.getSortField());
        requestParam.put("createdByMe", String.valueOf(request.getCreatedByMe()));
        return httpClient.get("/list_projects", token, requestParam, ListProjectsResponse.class);
    }

    public ProjectResponse createProject(CreateProjectRequest request)
            throws IOException, LitmusApiException {
        return httpClient.post("/create_project", token, request, ProjectResponse.class);
    }

    public CommonResponse updateProjectName(ProjectNameRequest request)
            throws IOException, LitmusApiException {
        return httpClient.post("/update_project_name", token, request, CommonResponse.class);
    }

    public ProjectResponse getProject(String projectId) throws IOException, LitmusApiException {
        return httpClient.get("/get_project/" + projectId, token, ProjectResponse.class);
    }

    public List<ProjectResponse> getOwnerProjects() throws IOException, LitmusApiException {
        TypeToken<List<ProjectResponse>> typeToken = new TypeToken<List<ProjectResponse>>() {
        };
        return httpClient.get("/get_owner_projects", token, typeToken);
    }

    public CommonResponse leaveProject(LeaveProjectRequest request)
            throws IOException, LitmusApiException {
        return httpClient.post("/leave_project", token, request, CommonResponse.class);
    }

    public ProjectRoleResponse getProjectRole(String projectId)
            throws IOException, LitmusApiException {
        return httpClient.get("/get_project_role/" + projectId, token, ProjectRoleResponse.class);
    }

    public UserWithProjectResponse getUserWithProject(String username)
            throws IOException, LitmusApiException {
        return httpClient.get("/get_user_with_project/" + username, token,
                UserWithProjectResponse.class);
    }

    public List<ProjectsStatsResponse> getProjectsStats() throws IOException, LitmusApiException {
        TypeToken<List<ProjectsStatsResponse>> typeToken = new TypeToken<List<ProjectsStatsResponse>>() {
        };
        return httpClient.get("/get_projects_stats", token, typeToken);
    }

    public List<ProjectMemberResponse> getProjectMembers(String projectID, String status)
            throws IOException, LitmusApiException {
        TypeToken<List<ProjectMemberResponse>> typeToken = new TypeToken<List<ProjectMemberResponse>>() {
        };
        return httpClient.get("/get_project_members/" + projectID + "/" + status, token, typeToken);
    }

    public List<ProjectMemberResponse> getProjectOwners(String projectID)
            throws IOException, LitmusApiException {
        TypeToken<List<ProjectMemberResponse>> typeToken = new TypeToken<List<ProjectMemberResponse>>() {
        };
        return httpClient.get("/get_project_owners/" + projectID, token, typeToken);
    }

    public CommonResponse deleteProject(String projectID) throws IOException, LitmusApiException {
        return httpClient.post("/delete_project/" + projectID, token, CommonResponse.class);
    }

    public SendInvitationResponse sendInvitation(SendInvitationRequest request) throws IOException, LitmusApiException {
        return httpClient.post("/send_invitation", token, request, SendInvitationResponse.class);
    }

    public CommonResponse acceptInvitation(AcceptInvitationRequest request) throws IOException, LitmusApiException {
        return httpClient.post("/accept_invitation", token, request, CommonResponse.class);
    }

    public CommonResponse declineInvitation(DeclineInvitationRequest request) throws IOException, LitmusApiException {
        return httpClient.post("/decline_invitation", token, request, CommonResponse.class);
    }

    public CommonResponse removeInvitation(RemoveInvitationRequest request) throws IOException, LitmusApiException {
        return httpClient.post("/remove_invitation", token, request, CommonResponse.class);
    }

    public List<ListInvitationResponse> listInvitation(String status)
            throws IOException, LitmusApiException {
        return httpClient.get("/list_invitations_with_filters/" + status, token, List.class);
    }

    public List<InviteUsersResponse> inviteUsers(String projectId)
            throws IOException, LitmusApiException {
        return httpClient.get("/invite_users/" + projectId, token, List.class);
    }

    // Environment
    public Environment getEnvironment(GetEnvironmentGraphQLQuery query, GetEnvironmentProjectionRoot projectionRoot){
        String request = new GraphQLQueryRequest(query, projectionRoot).serialize();
        GraphQLResponse response = graphQLClient.query(request);
        return response.extractValueAsObject("data.getEnvironment", new TypeRef<Environment>(){});
    }

    public ListEnvironmentResponse listEnvironments(ListEnvironmentsGraphQLQuery query, ListEnvironmentsProjectionRoot projectionRoot){
        String request = new GraphQLQueryRequest(query, projectionRoot).serialize();
        GraphQLResponse response = graphQLClient.query(request);
        System.out.println(response);
        return response.extractValueAsObject("data.listEnvironments", new TypeRef<ListEnvironmentResponse>(){});
    }

    public Environment createEnvironment(CreateEnvironmentGraphQLQuery query, CreateEnvironmentProjectionRoot projectionRoot){
        String request = new GraphQLQueryRequest(query, projectionRoot).serialize();
        GraphQLResponse response = graphQLClient.query(request);
        return response.extractValueAsObject("data.createEnvironment", new TypeRef<Environment>() {});
    }

    public String deleteEnvironment(DeleteEnvironmentGraphQLQuery query){
        String request = new GraphQLQueryRequest(query).serialize();
        GraphQLResponse response = graphQLClient.query(request);
        return response.extractValue("data.deleteEnvironment");
    }

    public String updateEnvironment(UpdateEnvironmentGraphQLQuery query){
        String request = new GraphQLQueryRequest(query).serialize();
        GraphQLResponse response = graphQLClient.query(request);
        return response.extractValue("data.updateEnvironment");
    }

    // Chaos Infrastructure
    public ListInfraResponse listInfras(ListInfrasGraphQLQuery query, ListInfrasProjectionRoot projectionRoot) {
        String request = new GraphQLQueryRequest(query, projectionRoot).serialize();
        GraphQLResponse response = graphQLClient.query(request);
        return response.extractValueAsObject("data.listInfras", new TypeRef<ListInfraResponse>() {});
    }

    public Infra getInfraDetails(GetInfraDetailsGraphQLQuery query, GetInfraDetailsProjectionRoot projectionRoot){
        String request = new GraphQLQueryRequest(query, projectionRoot).serialize();
        GraphQLResponse response = graphQLClient.query(request);
        return response.extractValueAsObject("data.getInfraDetails", new TypeRef<Infra>(){});
    }

    public GetInfraStatsResponse getInfraStats(GetInfraDetailsGraphQLQuery query, GetInfraDetailsProjectionRoot projectionRoot){
        String request = new GraphQLQueryRequest(query, projectionRoot).serialize();
        GraphQLResponse response = graphQLClient.query(request);
        return response.extractValueAsObject("data.getInfraStats", new TypeRef<GetInfraStatsResponse>(){});
    }

    public String getInfraManifest(GetInfraManifestGraphQLQuery query){
        String request = new GraphQLQueryRequest(query).serialize();
        GraphQLResponse response = graphQLClient.query(request);
        return response.extractValue("data.getInfraManifest");
    }

    public ConfirmInfraRegistrationResponse confirmInfraRegistration(ConfirmInfraRegistrationGraphQLQuery query, ConfirmInfraRegistrationProjectionRoot projectionRoot){
        String request = new GraphQLQueryRequest(query, projectionRoot).serialize();
        GraphQLResponse response = graphQLClient.query(request);
        return response.extractValueAsObject("data.confirmInfraRegistration", new TypeRef<ConfirmInfraRegistrationResponse>(){});
    }

    public String deleteInfra(DeleteInfraGraphQLQuery query){
        String request = new GraphQLQueryRequest(query).serialize();
        GraphQLResponse response = graphQLClient.query(request);
        return response.extractValue("data.deleteInfra");
    }

    public RegisterInfraResponse registerInfra(RegisterInfraGraphQLQuery query, RegisterInfraProjectionRoot projectionRoot){
        String request = new GraphQLQueryRequest(query, projectionRoot).serialize();
        GraphQLResponse response = graphQLClient.query(request);
        return response.extractValueAsObject("data.registerInfra", new TypeRef<RegisterInfraResponse>(){});
    }

    // Chaos Hub
    public List<ChaosHub> listChaosHub(ListChaosHubGraphQLQuery query, ListChaosHubProjectionRoot projectionRoot){
        String request = new GraphQLQueryRequest(query, projectionRoot).serialize();
        GraphQLResponse response = graphQLClient.query(request);
        return response.extractValueAsObject("data.listChaosHub", new TypeRef<List<ChaosHub>>(){});
    }

    public ChaosHub getChaosHub(GetChaosHubGraphQLQuery query, GetChaosHubProjectionRoot projectionRoot){
        String request = new GraphQLQueryRequest(query, projectionRoot).serialize();
        GraphQLResponse response = graphQLClient.query(request);
        return response.extractValueAsObject("data.getChaosHub", new TypeRef<ChaosHub>() {});
    }

    public GetChaosHubStatsResponse getChaosHubStats(GetChaosHubStatsGraphQLQuery query, GetChaosHubStatsProjectionRoot projectionRoot){
        String request = new GraphQLQueryRequest(query, projectionRoot).serialize();
        GraphQLResponse response = graphQLClient.query(request);
        return response.extractValueAsObject("data.getChaosHubStats", new TypeRef<GetChaosHubStatsResponse>() {});
    }

    public ChaosHub addChaosHub(AddChaosHubGraphQLQuery query, AddChaosHubProjectionRoot projectionRoot){
        String request = new GraphQLQueryRequest(query, projectionRoot).serialize();
        GraphQLResponse response = graphQLClient.query(request);
        return response.extractValueAsObject("data.addChaosHub", new TypeRef<ChaosHub>(){});
    }

    public ChaosHub addRemoteChaosHub(AddRemoteChaosHubGraphQLQuery query, AddRemoteChaosHubProjectionRoot projectionRoot){
        String request = new GraphQLQueryRequest(query, projectionRoot).serialize();
        GraphQLResponse response = graphQLClient.query(request);
        return response.extractValueAsObject("data.addRemoteChaosHub", new TypeRef<ChaosHub>() {});
    }

    public String deleteChaosHub(DeleteChaosHubGraphQLQuery query){
        String request = new GraphQLQueryRequest(query).serialize();
        GraphQLResponse response = graphQLClient.query(request);
        return response.extractValue("data.deleteChaosHub");
    }

    public ChaosHub saveChaosHub(SaveChaosHubGraphQLQuery query, SaveChaosHubProjectionRoot projectionRoot){
        String request = new GraphQLQueryRequest(query, projectionRoot).serialize();
        GraphQLResponse response = graphQLClient.query(request);
        return response.extractValueAsObject("data.saveChaosHub", new TypeRef<ChaosHub>() {});
    }

    public String syncChaosHub(SyncChaosHubGraphQLQuery query){
        String request = new GraphQLQueryRequest(query).serialize();
        GraphQLResponse response = graphQLClient.query(request);
        return response.extractValue("data.syncChaosHub");
    }

    public ChaosHub updateChaosHub(UpdateChaosHubGraphQLQuery query, UpdateChaosHubProjectionRoot projectionRoot){
        String request = new GraphQLQueryRequest(query, projectionRoot).serialize();
        GraphQLResponse response = graphQLClient.query(request);
        return response.extractValueAsObject("data.addRemoteChaosHub", new TypeRef<ChaosHub>() {});
    }

    // Chaos Experiment

    // Chaos Experiment Run

    // GitOps

    // Image Registry

    // Probe

    private String sanitizeURL(String url) {
        // TODO: need to add a validate URL without protocol
        // edge case: If you're calling a service from within Kubernetes, you don't need a protocol.
        return url.replaceAll("/$", "");
    }
}