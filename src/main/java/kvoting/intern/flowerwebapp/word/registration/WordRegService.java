package kvoting.intern.flowerwebapp.word.registration;

import kvoting.intern.flowerwebapp.item.Item;
import kvoting.intern.flowerwebapp.item.registration.Registration;
import kvoting.intern.flowerwebapp.item.registration.RegistrationService;
import kvoting.intern.flowerwebapp.word.Word;
import kvoting.intern.flowerwebapp.word.WordService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class WordRegService extends RegistrationService {
    public WordRegService(WordRegRepository wordRegRepository, ModelMapper modelMapper, WordService wordService) {
        super(wordRegRepository, modelMapper, wordService);
        this.regClazz = WordReg.class;
        this.itemClazz = Word.class;
    }

    @Override
    public void validateItem(Item item) {

    }

    @Override
    public void update(Registration registration, Item item) {

    }
}
