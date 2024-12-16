# MtS Modding Anniversary 7: Spire Café
## Preamble
A group project for the seventh anniversary of Mod the Spire. During act transitions the player may choose to go the vaunted Spire Café at a cost. Within they will be able to interact with patrons, attraction, merchants and the bartender, of course.  
For a full write-up, please see the [Design Doc](https://docs.google.com/document/d/1MN2Hh8NqupNfpMXXp6IdBxrz0AaZ2eMWuggbWKGvZLE/edit?usp=sharing)  
For a list of contributions, take a look at the [Contributions List](https://docs.google.com/spreadsheets/d/1PgRwGs0OWx8RKYv1QEsrOm7HJdfaqULHRM5qSSHo_yU/edit?usp=sharing)
  
## Contribution
Either modargo, Mindbomber or I (erasels) will be reviewing your pull request and suggest changes to code and/or design and balance if needed to keep the project cohesive. Since the start of this project is during the Christmas holidays, we may be slow to respond but don't worry, we will.
  
Some guidelines to follow to prevent a discussion that would happen during the PR process:  
- Bartenders must offer a healing service of some kind and may offer a second gameplay affecting service, these services must cost the player something, but you can be creative with the costs and effects  
- Merchants need to sell at least 3 services (these can be card modifications, relics, or potions, etc). If you let them sell items, don't go overboard and don't create more than 20 custom items for them to sell  
- A Patron shouldn't offer more than 3 options that affect gameplay and may only have a single gameplay affecting interaction per visit  
- An attraction needs to do something, it cannot just be a static image or only dispense a single line of text. Attractions are a wildcard, they can do whatever.  
- All gameplay affecting interactions must have a cost, we can't give the player free power cost generally.  
- In general avoid NPCs or attractions that are simple one-liners with no special (does not have to affect gameplay, see Makeup table making you sparkle for the rest of the run) effects  
- All cutscenes must be exitable by the player without doing a transaction  

### Technical Guidelines
All the relevant abstracts can be found in the abstracts package, take a look at them and the existing examples for each type to get a feel for how to use the custom systems.  

To create an interactable, first decide which of the categories it falls into.  
- Merchant (has a shop screen and sells services) Stands to the left. One per visit.
- Bartender (Has a healing option and may have a second gameplay affecting option, both can be selected) Stands on top of the bar. One per visit.
- Patron (may have multiple gameplay affecting options but will only allow one) Stand before the bar. Up to 3 exist during a visit.
- Attraction (Wild card, these can do many different things) Stands between merchant and bar. One per visit(?).
  
Create a package in the package of the interactables listed above and create your class within while extending the correct abstract. Merchants and Bartenders have a lot of custom logic,
but Patrons and Attractions are rather freeform, every abstract other than the Merchant has access to the cutscene system. 
  
Images unique to your interactable should be saved in `...Resources/images/[type]/[interactable]/`.  
Localization is saved in `localization/[langKey]/[interactable]/`.  
  
To test your contribution, you can force it to spawn by modifying the CafeRoom:onEnterRoom logic and spawning the event with `event anniv7:CafeRoom`.


#### Cards, relics, powers, etc.
Cards, relics, powers, patches, and everything else should go in the package you created for your interactable.

There are abstract classes that you should extend in the abstracts package: `AbstractSCCard`, `AbstractSCRelic`, and `AbstractSCPower`.
  

### How to make PRs  
To make a contribution, you must have a GitHub account. 
For the specifics of how to fork this repo and then make a pull request, please look at this guide:  
https://docs.github.com/en/get-started/quickstart/contributing-to-projects  
   
I recommend using the GitHub desktop client for this if you have no experience with Github  
https://desktop.github.com/
