package kvoting.intern.flowerwebapp.word.registration;

import kvoting.intern.flowerwebapp.registration.ProcessType;
import kvoting.intern.flowerwebapp.registration.Registration;
import kvoting.intern.flowerwebapp.registration.RegistrationType;
import kvoting.intern.flowerwebapp.word.Word;
import kvoting.intern.flowerwebapp.word.WordBase;
import kvoting.intern.flowerwebapp.word.WordService;
import kvoting.intern.flowerwebapp.word.registration.request.WordRegistRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
@Transactional
public class WordRegService {
    private final WordRegRepository wordRegRepository;
    private final WordService wordService;
    private final ModelMapper modelMapper;

    public WordReg getWordReg(Long id) {
        return wordRegRepository.findById(id).orElseThrow(() -> {
            throw new RuntimeException();
        });
    }

    public WordReg create(WordRegistRequest request) {
        WordBase wordBase = modelMapper.map(request, WordBase.class);
        Word word = new Word();
        word.setWordBase(wordBase);
        word.setStatus(ProcessType.UNHANDLED);
        word = wordService.save(word);

        return wordRegRepository.save(generateWordReg(request, word,
                RegistrationType.CREATE));
    }

    public WordReg modify(WordRegistRequest request, Long id) {
        Word word = wordService.getWord(id);
        return wordRegRepository.save(generateWordReg(request, word,
                RegistrationType.MODIFY));
    }

    public WordReg delete(Long id) {
        Word word = wordService.getWord(id);
        return wordRegRepository.save(generateWordReg(word));
    }

    public WordReg processWordReg(Long id, ProcessType type) {
        WordReg wordReg = getWordReg(id);
        wordReg.getRegistration().setProcessType(type);
        if (type == ProcessType.REJECTED) {
            if (wordReg.getRegistration().getRegistrationType() == RegistrationType.CREATE) {
                wordReg.getWord().setStatus(type);
            }
        }
        if (type == ProcessType.APPROVED) {
            if (wordReg.getRegistration().getRegistrationType() == RegistrationType.CREATE) {
                wordReg.getWord().setStatus(type);
            }
            if (wordReg.getRegistration().getRegistrationType() == RegistrationType.MODIFY) {
                Word word = wordReg.getWord();
                modelMapper.map(wordReg.getWordBase(), word);
                word = wordService.save(word);
                wordReg.setWord(word);
            }
            if (wordReg.getRegistration().getRegistrationType() == RegistrationType.DELETE) {
                wordService.deleteWord(wordReg.getWord());
                return null;
            }
        }
        return wordRegRepository.save(wordReg);
    }


    private WordReg generateWordReg(Word word) {
        return WordReg.builder()
                .word(word)
                .registration(generateReg(RegistrationType.DELETE))
                .build();
    }

    private WordReg generateWordReg(WordRegistRequest request, Word word, RegistrationType type) {
        WordBase wordBase = modelMapper.map(request, WordBase.class);
        return WordReg.builder()
                .word(word)
                .wordBase(wordBase)
                .registration(generateReg(type))
                .build();
    }

    private Registration generateReg(RegistrationType type) {
        return Registration.builder()
                .registrationType(type)
                .processType(ProcessType.UNHANDLED)
                .build();
    }

}
