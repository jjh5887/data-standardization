package kvoting.intern.flowerwebapp.word.registration;

import kvoting.intern.flowerwebapp.registration.Registration;
import kvoting.intern.flowerwebapp.registration.RegistrationService;
import kvoting.intern.flowerwebapp.word.Word;
import kvoting.intern.flowerwebapp.word.WordBase;
import kvoting.intern.flowerwebapp.word.WordService;
import kvoting.intern.flowerwebapp.word.registration.request.WordRegistRequest;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WordRegService extends RegistrationService<WordRegistRequest, Word, WordBase> {
    public WordRegService(WordRegRepository wordRegRepository, ModelMapper modelMapper, WordService wordService) {
        super(wordRegRepository, modelMapper, wordService);
        this.regClazz = WordReg.class;
    }

    @Override
    public Word getItem(Registration registration) {
        return ((WordReg) registration).getWord();
    }

    @Override
    public WordBase getBase(Registration registration) {
        return ((WordReg) registration).getWordBase();
    }

    @Override
    public void setItem(Registration registration, Word word) {
        ((WordReg) registration).setWord(word);
    }

    @Override
    public void update(Registration registration, Word item) {

    }
}
