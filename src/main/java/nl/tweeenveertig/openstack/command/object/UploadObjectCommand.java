package nl.tweeenveertig.openstack.command.object;

import nl.tweeenveertig.openstack.client.Account;
import nl.tweeenveertig.openstack.command.core.httpstatus.HttpStatusFailCondition;
import nl.tweeenveertig.openstack.command.core.httpstatus.HttpStatusSuccessCondition;
import nl.tweeenveertig.openstack.headers.object.ObjectContentType;
import nl.tweeenveertig.openstack.model.UploadInstructions;
import nl.tweeenveertig.openstack.exception.CommandException;
import nl.tweeenveertig.openstack.command.core.httpstatus.HttpStatusChecker;
import nl.tweeenveertig.openstack.command.core.httpstatus.HttpStatusMatch;
import nl.tweeenveertig.openstack.command.identity.access.Access;
import nl.tweeenveertig.openstack.client.Container;
import nl.tweeenveertig.openstack.client.StoredObject;
import nl.tweeenveertig.openstack.headers.object.Etag;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.params.CoreProtocolPNames;

import java.io.IOException;

public class UploadObjectCommand extends AbstractObjectCommand<HttpPut, Object> {

    public UploadObjectCommand(Account account, HttpClient httpClient, Access access, Container container,
                               StoredObject target, UploadInstructions uploadInstructions) {
        super(account, httpClient, access, container, target);
        try {
            prepareUpload(uploadInstructions);
        } catch (IOException err) {
            throw new CommandException("Unable to open input stream for uploading", err);
        }
    }

    protected void prepareUpload(UploadInstructions uploadInstructions) throws IOException {
        HttpEntity entity = uploadInstructions.getEntity();
        setHeader(uploadInstructions.getDeleteAt());
        setHeader(uploadInstructions.getDeleteAfter());
        setHeader(uploadInstructions.getObjectManifest());
        if (uploadInstructions.getMd5() != null) {
            setHeader(new Etag(uploadInstructions.getMd5()));
        }
        if (uploadInstructions.getContentType() != null) {
            setHeader(new ObjectContentType(uploadInstructions.getContentType()));
        }
        else if (!(entity instanceof InputStreamEntity)) { // reading an InputStream is not a smart idea
            setHeader(new Etag(entity.getContent()));
        }
        request.setEntity(entity);
    }

    @Override
    protected HttpPut createRequest(String url) {
        HttpPut putMethod = new HttpPut(url);
        putMethod.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, true);
        return putMethod;
    }

    @Override
    protected HttpStatusChecker[] getStatusCheckers() {
        return new HttpStatusChecker[] {
            new HttpStatusSuccessCondition(new HttpStatusMatch(HttpStatus.SC_CREATED)),
            new HttpStatusFailCondition(new HttpStatusMatch(HttpStatus.SC_LENGTH_REQUIRED)),
            new HttpStatusFailCondition(new HttpStatusMatch(HttpStatus.SC_NOT_FOUND)),
            new HttpStatusFailCondition(new HttpStatusMatch(HttpStatus.SC_UNPROCESSABLE_ENTITY))
        };
    }

}
