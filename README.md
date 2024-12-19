# MtS Modding Anniversary 7: Spire Café
## Preamble
A group project for the seventh anniversary of Mod the Spire. Between acts the player stops off at the renowned Spire Café, at the cost of getting a bit less healing. Within they will be able to interact with patrons, attractions, merchants and the bartender, of course.  
For a full write-up, please see the [Design Doc](https://docs.google.com/document/d/1MN2Hh8NqupNfpMXXp6IdBxrz0AaZ2eMWuggbWKGvZLE/edit?usp=sharing)  
For a list of contributions, take a look at the [Contributions List](https://docs.google.com/spreadsheets/d/1PgRwGs0OWx8RKYv1QEsrOm7HJdfaqULHRM5qSSHo_yU/edit?usp=sharing)
  
## Contributions
Either modargo, Mindbomber or I (erasels) will be reviewing your pull request and suggesting changes to code and/or design and balance if needed to keep the project cohesive. Since the start of this project is during the Christmas holidays, we may be slow to respond but don't worry, we will. Be aware that even after code is merged, maintainers may need to make bug fixes and balance changes as we get feedback (we'll do our best to consult contributors for balance changes). Collaborating with others to make a contribution is fine.

Most contributions will be one of the following types of interactable game elements in the Café:
- Merchant: has a shop screen and sells services. Stands to the left. One per visit.
- Bartender: has a healing option and may have a second gameplay affecting option, both can be used in a single visit. Stands on top of the bar. One per visit.
- Patron: may have multiple gameplay affecting options but will only allow one per visit. Stands in front of the bar. Up to three per visit.
- Attraction: wild card, can do many different things and may be purely flavor. Stands between merchant and bar. One per visit.

If you're thinking of a contribution that isn't of these, it's best to discuss it with us first, to make sure we're on the same page about how to approach it.
  
### Technical Guidelines
Abstract clases for each type of interactable element can be found in the abstracts package: `AbstractMerchant`, `AbstractBartender`, `AbstractPatron`, and `AbstractAttraction`. Take a look at them and the existing examples for each type to get a feel for how to use the custom systems we've made for this mod.
  
For your contribution, create a package in the right location for the type of interactable you're making (e.g. a new Merchant would go in a new package under `spireCafe/interactables/merchants/`) and create your class within that, extending the abstract for that type. Merchants and Bartenders have a lot of custom logic, but Patrons and Attractions are rather freeform. Every abstract other than the Merchant has access to the cutscene system. Again, check out the examples to see how the systems can be used.
  
Images unique to your interactable should be saved in `anniv7Resources/images/[type]/[interactable]/`.  
Localization is saved in `anniv7Resources/localization/[langKey]/[interactable]/`.  
  
To test your contribution, you can force it to spawn by modifying the CafeRoom:onEnterRoom logic and spawning the event with `event anniv7:CafeRoom`.  
You can also guarantee your interactable to spawn with the new `cafe [type] [?slot]` console command.  
**Please make sure to add your interactable to the [Contributions List](https://docs.google.com/spreadsheets/d/1PgRwGs0OWx8RKYv1QEsrOm7HJdfaqULHRM5qSSHo_yU/edit?usp=sharing) before your PR. If it's an idea you want to code yourself, you can add it there even without having started coding it.**

### Contribution guidelines
To make suitable content and help the PR process go smoothly, follow these guidelines:
- Bartenders must offer a healing service of some kind and may offer a second gameplay affecting service, these services must cost the player something, but you can be creative with the costs and effects  
- Merchants need to sell at least 3 services (these can be card modifications, relics, or potions, etc). If you let them sell items, don't go overboard and don't create more than 20 custom items for them to sell  
- A Patron shouldn't offer more than 3 options that affect gameplay and may only have a single gameplay affecting interaction per visit  
- An Attraction needs to do something, it cannot just be a static image or only dispense a single line of text. Attractions are a wildcard, they can do whatever.  
- All gameplay affecting interactions must have a cost, no free power increases (and keep things reasonably balanced, the Cafe should not be a huge power level boost)
- In general avoid NPCs or attractions that are simple one-liners with no special effects (but non-gameplay affects are fine, such as Makeup Table making you sparkle for the rest of the run)
- All cutscenes must be exitable by the player without doing a transaction
- No combats in the Café, there are too many edge cases and the code is complex enough already
- We expect contributions to be complete (including art) before merging, but it's okay to make a PR while still working on the art

#### Adding a Page to the Bookshelf
To add a Page to the bookshelf you have to make a new class in the `spireCafe/interactables/attractions/bookshelf/pages` package that extends the AbstractPage class. The class contains many features which you can make use of to add a bit of logic to your pages.  
For the actual text you'll want to add an entry to `anniv7Resources/localization/[langKey]/BookshelfAttraction/UIstrings.json` with the key being the same as the ID of your Page class.

#### Cards, relics, powers, etc.
Cards, relics, powers, patches, and everything else should go in the package you created for your interactable.

There are abstract classes that you should extend in the abstracts package: `AbstractSCCard`, `AbstractSCRelic`, and `AbstractSCPower`.
  
### How to make PRs  
To make a contribution, you must have a GitHub account. 
For the specifics of how to fork this repo and then make a pull request, please look at this guide:  
https://docs.github.com/en/get-started/quickstart/contributing-to-projects  
   
I recommend using the GitHub desktop client for this if you have no experience with Github  
https://desktop.github.com/
