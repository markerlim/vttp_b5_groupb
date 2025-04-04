package vttp.batch5.ssf.day15.repositories;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import vttp.batch5.ssf.day15.models.Contact;

@Repository
public class ContactRepository {

   // DI the RedisTemplate into ContactRepository
   @Autowired @Qualifier("redis-object")
   private RedisTemplate<String, Object> template;

   // hgetall id
   public Optional<Contact> getContactById(String id) {
      HashOperations<String, String, Object> hashOps = template.opsForHash();
      Map<String, Object> contact = hashOps.entries(id);

      if (contact.isEmpty())
         return Optional.empty();

      Contact result = new Contact();
      result.setName(contact.get("name").toString());
      result.setPhone(contact.get("phone").toString());
      result.setEmail(contact.get("email").toString());

      return Optional.of(result);
   }

   // keys *
   public Set<String> getContactIds() {
      return template.keys("*");
   }

   // 
   // hset abc123 name fred
   // hset abc123 email fred@gmail.com
   public void insertContact(Contact contact) {

      //ListOperations<String, Object> listOps = template.opsForList();
      //ValueOperations<String, Object> valueOps = template.opsForValue();

      // key serilaizer, hash key, 
      HashOperations<String, String, Object> hashOps = template.opsForHash();
      //hashOps.put(contact.getId(), "name", contact.getName());
      //hashOps.put(contact.getId(), "email", contact.getEmail());
      //hashOps.put(contact.getId(), "phone", contact.getPhone());

      Map<String, Object> values = new HashMap<>();
      values.put("name", contact.getName());
      values.put("email", contact.getEmail());
      values.put("phone", contact.getPhone());
      hashOps.putAll(contact.getId(), values);
   }

}
