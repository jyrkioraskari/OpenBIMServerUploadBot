package de.rwth_aachen.dc.bimserver.rest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.bimserver.client.BimServerClient;
import org.bimserver.client.json.JsonBimServerClientFactory;
import org.bimserver.interfaces.objects.SDeserializerPluginConfiguration;
import org.bimserver.interfaces.objects.SObjectState;
import org.bimserver.interfaces.objects.SProject;
import org.bimserver.shared.ChannelConnectionException;
import org.bimserver.shared.UsernamePasswordAuthenticationInfo;
import org.bimserver.shared.exceptions.BimServerClientException;
import org.bimserver.shared.exceptions.PublicInterfaceNotFoundException;
import org.bimserver.shared.exceptions.ServiceException;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.google.gson.Gson;

import de.rwth_aachen.dc.BimServerPasswords;

/*
 * Jyrki Oraskari, 2020
 */

@Path("/")
public class BIMServerModelUpdate_OpenAPI {

	

	@GET
	@Path("/services")
	@Produces(MediaType.APPLICATION_JSON)
	public Response services_list(@Context UriInfo uriInfo) {
		List<Service> services = new ArrayList<>();
		System.out.println(uriInfo.getBaseUri().getHost());
	    System.out.println(uriInfo.getBaseUri().getPort());
		services.add(new Service(3031,"BIMServerUpload", "BIMServer IFC Model Upload, default project", "BIM4REN", null, "http://"+uriInfo.getBaseUri().getHost()+"/BIMServerIFCFileAPI/api/upload", null, null));
		services.add(new Service(3032,"BIMServerUpload", "BIMServer IFC Model Upload", "BIM4REN", null, "http://"+uriInfo.getBaseUri().getHost()+"/BIMServerIFCFileAPI/api/upload/{project_id}", null, null));
		services.add(new Service(3033,"BIMServerDownload", "BIMServer IFC Model Download, default project", "BIM4REN", null, "http://"+uriInfo.getBaseUri().getHost()+"/BIMServerIFCFileAPI/api/download", null, null));
		services.add(new Service(3034,"BIMServerDownload", "BIMServer IFC Model Download", "BIM4REN", null, "http://"+uriInfo.getBaseUri().getHost()+"/BIMServerIFCFileAPI/api/download/{project_id}", null, null));
	    String json = new Gson().toJson(services);
		return Response.ok(json, "application/json").build();

	}
	

	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({"application/ld+json"})
	public Response uploadIFCtoProjectAsMultiPartFormData(@HeaderParam(HttpHeaders.ACCEPT) String accept_type,@FormDataParam("ifcFile") InputStream ifcFile) {
		try {
			File tempIfcFile = File.createTempFile("bimserver-", ".ifc");
			tempIfcFile.deleteOnExit();

			Files.copy(ifcFile, tempIfcFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			IOUtils.closeQuietly(ifcFile);
			uploadRelease("default",tempIfcFile.toPath());
			return Response.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return Response.noContent().build();
	}
	
	

	@POST
	@Path("/upload")
	@Consumes({ MediaType.TEXT_PLAIN, "application/ifc" })
	@Produces({"application/ld+json"})
	public Response uploadIFCtoProjectAsTxt(@HeaderParam(HttpHeaders.ACCEPT) String accept_type, String ifc_step_content) {
		String project_id="default";
		try {
			File tempIfcFile = File.createTempFile("bimserver-", ".ifc");
			tempIfcFile.deleteOnExit();
			try {
			      FileWriter myWriter = new FileWriter(tempIfcFile);
			      myWriter.write(ifc_step_content);
			      myWriter.close();
			    } catch (IOException e) {
			      e.printStackTrace();
			    }
			uploadRelease("default",tempIfcFile.toPath());
			return Response.ok().build();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return Response.noContent().build();
	}
	
	
	@POST
	@Path("/upload/{project_id}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({"application/ld+json"})
	public Response uploadIFCtoProjectAsMultiPartFormData(@HeaderParam(HttpHeaders.ACCEPT) String accept_type,@FormDataParam("ifcFile") InputStream ifcFile,@PathParam("project_id") String project_id) {
		try {
			File tempIfcFile = File.createTempFile("bimserver-", ".ifc");
			tempIfcFile.deleteOnExit();

			Files.copy(ifcFile, tempIfcFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			IOUtils.closeQuietly(ifcFile);
			uploadRelease(project_id,tempIfcFile.toPath());
			return Response.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return Response.noContent().build();
	}
	
	
	
	@POST
	@Path("/upload/{project_id}")
	@Consumes({ MediaType.TEXT_PLAIN, "application/ifc" })
	@Produces({"application/ld+json"})
	public Response uploadIFCtoProjectAsTxt(@HeaderParam(HttpHeaders.ACCEPT) String accept_type, String ifc_step_content,@PathParam("project_id") String project_id) {
		try {
			File tempIfcFile = File.createTempFile("bimserver-", ".ifc");
			tempIfcFile.deleteOnExit();
			try {
			      FileWriter myWriter = new FileWriter(tempIfcFile);
			      myWriter.write(ifc_step_content);
			      myWriter.close();
			    } catch (IOException e) {
			      e.printStackTrace();
			    }
			uploadRelease(project_id,tempIfcFile.toPath());
			return Response.ok().build();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return Response.noContent().build();
	}
	
	

	@GET
	@Path("/download")
	@Produces({ MediaType.TEXT_PLAIN, "application/ifc" })
	public Response downloadIFCFromProject(@HeaderParam(HttpHeaders.ACCEPT) String accept_type) {
		String project_id="default";
		return Response.ok().build();
	}
	

	

	@GET
	@Path("/download/{project_id}")
	@Produces({ MediaType.TEXT_PLAIN, "application/ifc" })
	public Response downloadIFCFromProject(@HeaderParam(HttpHeaders.ACCEPT) String accept_type,@PathParam("project_id") String project_id) {
		return Response.ok().build();
	}
	

   
	public File uploadRelease(String projectName, java.nio.file.Path file) {
		try {
			JsonBimServerClientFactory factory = new JsonBimServerClientFactory("http://localhost:8090");
			BimServerClient client = factory.create(
					new UsernamePasswordAuthenticationInfo(BimServerPasswords.user, BimServerPasswords.password));

			List<SProject> projects = client.getServiceInterface().getAllReadableProjects();
			boolean project_exists=false;
			for (SProject p : projects) {
				if (p.getState() == SObjectState.ACTIVE)
					if (p.getName().equals(projectName)) {
						project_exists=true;
						break;
					}
			}
			
			if(!project_exists)
			{
				SProject p =client.getServiceInterface().addProject(projectName, "ifc2x3tc1"); 
				
				SDeserializerPluginConfiguration deserialize=client.getServiceInterface().getSuggestedDeserializerForExtension("ifc", p.getOid());
				
				client.checkinSync(p.getOid(), "AUTOMATIC UPDATE", deserialize.getOid(), false, file);
				
			}
			
		} catch (BimServerClientException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (ChannelConnectionException e) {
			e.printStackTrace();
		} catch (PublicInterfaceNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return null;
	}

	/*
	public File downloadLastRelease(String projectName) {
		try {
			JsonBimServerClientFactory factory = new JsonBimServerClientFactory("http://localhost:8090");
			BimServerClient client = factory.create(
					new UsernamePasswordAuthenticationInfo(BimServerPasswords.user, BimServerPasswords.password));

			List<SProject> projects = client.getServiceInterface().getAllReadableProjects();
			byte[] data = null;
			for (SProject p : projects) {
				if (p.getState() == SObjectState.ACTIVE)
					if (p.getLastRevisionId() >= 0 && p.getName().equals(projectName)) {

						System.out.println(p.getName() + " " + p.getState().name());
						SRevision revision = client.getServiceInterface().getRevision(p.getLastRevisionId());

						SSerializerPluginConfiguration serializerByContentType = client.getServiceInterface()
								.getSerializerByContentType("application/ifc");
						ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
						client.download(revision.getOid(), serializerByContentType.getOid(), outputStream);
						data = outputStream.toByteArray();
						System.out.println("len: " + data.length);

						File tempFile = File.createTempFile("ifc2lbd", ".ifc");
						FileOutputStream fo = new FileOutputStream(tempFile);
						fo.write(data);
						fo.close();
						return tempFile;
					}
			}
		} catch (BimServerClientException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (ChannelConnectionException e) {
			e.printStackTrace();
		} catch (PublicInterfaceNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		IFCUpdate_OpenAPI api=new IFCUpdate_OpenAPI();
		//api.uploadRelease("test10");
	}
*/
}