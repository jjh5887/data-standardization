package kvoting.intern.flowerwebapp.word.registration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import kvoting.intern.flowerwebapp.item.Item;
import kvoting.intern.flowerwebapp.item.registration.Registration;
import kvoting.intern.flowerwebapp.item.registration.RegistrationService;
import kvoting.intern.flowerwebapp.item.registration.request.RegRequest;
import kvoting.intern.flowerwebapp.word.Word;
import kvoting.intern.flowerwebapp.word.WordService;

@Service
@Primary
public class WordRegService extends RegistrationService {
	public WordRegService(WordRegRepository wordRegRepository, ModelMapper modelMapper, WordService wordService) {
		super(wordRegRepository, modelMapper, wordService);
		this.regClazz = WordReg.class;
		this.itemClazz = Word.class;
	}

	@Override
	public void setUpItem(Item item, RegRequest regRequest) {
	}

	@Override
	public void validateItem(Item item) {

	}

	@Override
	public void update(Registration registration, Item item) {
		WordReg wordReg = (WordReg)registration;
		Word word = (Word)item;
		word.setBase(wordReg.getBase());
	}

	@Override
	public void setUpReg(Registration registration, RegRequest request) {

	}
}
