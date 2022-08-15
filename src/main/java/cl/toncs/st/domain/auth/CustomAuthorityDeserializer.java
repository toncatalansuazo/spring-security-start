package cl.toncs.st.domain.auth;

import cl.toncs.st.entities.user.RoleType;
import cl.toncs.st.exception.InvalidRoleException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class CustomAuthorityDeserializer extends JsonDeserializer<Set<SimpleGrantedAuthority>> {

    @Override
    public Set<SimpleGrantedAuthority> deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode jsonNode = mapper.readTree(jp);
        Set<SimpleGrantedAuthority> grantedAuthorities = new HashSet<>();
        if (jsonNode.size() == 0) {
            throw new InvalidRoleException("Wrong field authorities: Invalid ROLE. Value in request = [].");
        }
        Iterator<JsonNode> elements = jsonNode.elements();
        while (elements.hasNext()) {
            JsonNode next = elements.next();
            JsonNode authority = next.get("authority");
            String roleName = authority.asText();
            boolean existRole = Arrays.stream(RoleType.values()).anyMatch(roleType -> roleType.name().equals(roleName));
            if (!existRole) {
                throw new InvalidRoleException(String.format("Wrong field authorities: Invalid ROLE. Value in request = [%s].", roleName));
            }
            if (!roleName.contains(RoleType.ROLE_ADMIN.name())) {
                grantedAuthorities.add(new SimpleGrantedAuthority(roleName));
            }
        }

        return grantedAuthorities.size() > 0 ? grantedAuthorities :
            null;
    }

}
