package kvoting.intern.flowerwebapp.word.registration;

import kvoting.intern.flowerwebapp.registration.Registration;
import kvoting.intern.flowerwebapp.registration.RegistrationType;
import kvoting.intern.flowerwebapp.type.ProcessType;
import kvoting.intern.flowerwebapp.word.Word;
import kvoting.intern.flowerwebapp.word.WordBase;
import kvoting.intern.flowerwebapp.word.WordService;
import kvoting.intern.flowerwebapp.word.registration.request.WordRegistRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
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
        // create mappedWord
        Word mappedWord = modelMapper.map(request, Word.class);
        mappedWord.setStatus(ProcessType.UNHANDLED);
        mappedWord.setWordRegs(new HashSet<>());
        Word word = wordService.save(mappedWord);

        // create word create_reg
        return wordRegRepository.save(generateWordReg(request, word, RegistrationType.CREATE));
    }

    public WordReg modify(WordRegistRequest request, Long id) {
        // find word
        Word word = wordService.getWord(id);

        // create word modify_reg
        return wordRegRepository.save(generateWordReg(request, word, RegistrationType.MODIFY));
    }

    public WordReg delete(Long id) {
        // find word
        Word word = wordService.getWord(id);

        // create word delete_reg
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
                modelMapper.map(wordReg.getRegWord(), word);
                word = wordService.save(word);
                wordReg.setWord(word);
                System.out.println(wordReg.getRegistration().getRegistrationType());
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
        WordBase regWord = modelMapper.map(request, WordBase.class);
        return WordReg.builder()
                .word(word)
                .regWord(regWord)
                .registration(generateReg(type))
                .build();
    }

    private Registration generateReg(RegistrationType type) {
        return Registration.builder()
                .register("admin")
                .registrationType(type)
                .processType(ProcessType.UNHANDLED)
                .dateRegistered(LocalDateTime.now())
                .build();
    }

}